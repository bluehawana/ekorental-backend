package com.bluehawana.rentingcarsys.service;

import com.bluehawana.rentingcarsys.dto.BookingDTO;
import com.bluehawana.rentingcarsys.dto.BookingResponseDTO;
import com.bluehawana.rentingcarsys.exception.BookingConfirmationException;
import com.bluehawana.rentingcarsys.exception.CarNotAvailableException;
import com.bluehawana.rentingcarsys.exception.PaymentException;
import com.bluehawana.rentingcarsys.exception.ResourceNotFoundException;
import com.bluehawana.rentingcarsys.model.Booking;
import com.bluehawana.rentingcarsys.model.BookingStatus;
import com.bluehawana.rentingcarsys.model.Car;
import com.bluehawana.rentingcarsys.model.User;
import com.bluehawana.rentingcarsys.repository.BookingRepository;
import com.bluehawana.rentingcarsys.repository.CarRepository;
import com.bluehawana.rentingcarsys.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final CarRepository carRepository;
    private final CarService carService;
    private final NotificationService notificationService;
    private final PaymentService paymentService;

    // ... (keep your existing method implementations)

    @Override
    public void processPayment(Long bookingId, String paymentIntentId) throws PaymentException {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));

        try {
            // Process payment
            paymentService.confirmPayment(paymentIntentId);

            // Update booking status
            booking.setStatus(BookingStatus.PAID);
            booking.setPaymentId(paymentIntentId);
            booking.setUpdatedAt(LocalDateTime.now());
            bookingRepository.save(booking);

            // Send confirmation email after successful payment
            notificationService.sendPaymentConfirmation(bookingId);
        } catch (Exception e) {
            booking.setStatus(BookingStatus.PAYMENT_FAILED);
            bookingRepository.save(booking);
            throw new PaymentException("Payment processing failed: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public void confirmBooking(Long id) throws BookingConfirmationException, CarNotAvailableException {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));

        // Verify car is still available
        if (!isCarAvailableForDates(booking.getCar().getId(), booking.getStartDate(), booking.getEndDate())) {
            throw new CarNotAvailableException("Car is no longer available for these dates");
        }

        try {
            // Update booking status
            booking.setStatus(BookingStatus.CONFIRMED);
            booking.setUpdatedAt(LocalDateTime.now());
            bookingRepository.save(booking);

            // Update car availability
            carService.updateCarAvailability(booking.getCar().getId(), false);

            // Send confirmation email
            notificationService.sendBookingConfirmation(booking);
        } catch (Exception e) {
            // Rollback in case of failure
            booking.setStatus(BookingStatus.CONFIRMATION_FAILED);
            bookingRepository.save(booking);
            throw new BookingConfirmationException("Failed to confirm booking: " + e.getMessage());
        }
    }

    @Override
    public void sendBookingConfirmation(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));
        notificationService.sendBookingConfirmation(booking);
    }

    @Override
    public boolean isCarAvailableForDates(Long carId, LocalDateTime startDate, LocalDateTime endDate) {
        Car car = carRepository.findById(carId)
                .orElseThrow(() -> new ResourceNotFoundException("Car not found"));

        if (!car.isAvailable()) {
            return false;
        }

        // Check for overlapping bookings
        List<Booking> overlappingBookings = bookingRepository.findOverlappingBookings(
                carId, startDate, endDate);

        return overlappingBookings.isEmpty();
    }

    @Override
    @Transactional
    public void cancelBooking(Long id) throws PaymentException {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));

        // Only allow cancellation of non-completed bookings
        if (booking.getStatus() == BookingStatus.COMPLETED) {
            throw new IllegalStateException("Cannot cancel a completed booking");
        }

        // Process refund if payment was made
        if (booking.getStatus() == BookingStatus.PAID && booking.getPaymentId() != null) {
            paymentService.processRefund(booking.getPaymentId());
        }

        // Update booking status
        booking.setStatus(BookingStatus.CANCELLED);
        booking.setUpdatedAt(LocalDateTime.now());
        bookingRepository.save(booking);

        // Make car available again if it was confirmed
        if (booking.getStatus() == BookingStatus.CONFIRMED) {
            carService.updateCarAvailability(booking.getCar().getId(), true);
        }

        // Send cancellation notification
        notificationService.sendBookingCancellation(booking);
    }

    @Override
    public BookingResponseDTO createBooking(Long userId, Long carId, BookingDTO bookingDTO) {
        return null;
    }

    @Override
    public List<BookingResponseDTO> getAllBookingsByUser() {
        return List.of();
    }

    @Override
    public List<BookingResponseDTO> getBookingsByUser(User user) {
        return List.of();
    }

    @Override
    public BookingResponseDTO getBookingById(Long id) {
        return null;
    }

    @Override
    public List<BookingResponseDTO> getAllBookings() {
        return List.of();
    }

    @Override
    public List<BookingResponseDTO> getBookingsByUserId(Long userId) {
        return List.of();
    }

    @Override
    public void deleteBooking(Long id) {

    }

    @Override
    public BookingResponseDTO updateBooking(Long id, BookingDTO bookingDTO) {
        return null;
    }

    @Override
    public BookingResponseDTO updateBooking(Long id, Booking bookingDTO) {
        return null;
    }

    @Override
    public void updateBookingStatus(Long id, BookingStatus status) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));

        // Validate status transition
        validateStatusTransition(booking.getStatus(), status);

        booking.setStatus(status);
        booking.setUpdatedAt(LocalDateTime.now());
        bookingRepository.save(booking);

        // Handle status-specific actions
        switch (status) {
            case CONFIRMED:
                carService.updateCarAvailability(booking.getCar().getId(), false);
                notificationService.sendBookingConfirmation(booking);
                break;
            case CANCELLED:
                carService.updateCarAvailability(booking.getCar().getId(), true);
                notificationService.sendBookingCancellation(booking);
                break;
            case COMPLETED:
                notificationService.sendBookingCompletionConfirmation(booking);
                break;
        }
    }

    private void validateStatusTransition(BookingStatus currentStatus, BookingStatus newStatus) {
        // Add your status transition validation logic here
        // For example:
        if (currentStatus == BookingStatus.CANCELLED && newStatus != BookingStatus.CANCELLED) {
            throw new IllegalStateException("Cannot change status of a cancelled booking");
        }
        if (currentStatus == BookingStatus.COMPLETED && newStatus != BookingStatus.COMPLETED) {
            throw new IllegalStateException("Cannot change status of a completed booking");
        }
    }
}