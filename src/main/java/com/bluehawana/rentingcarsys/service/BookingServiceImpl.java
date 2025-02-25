package com.bluehawana.rentingcarsys.service;

import com.bluehawana.rentingcarsys.dto.BookingDTO;
import com.bluehawana.rentingcarsys.dto.BookingResponseDTO;
import com.bluehawana.rentingcarsys.exception.BookingConfirmationException;
import com.bluehawana.rentingcarsys.exception.CarNotAvailableException;
import com.bluehawana.rentingcarsys.exception.PaymentException;
import com.bluehawana.rentingcarsys.exception.ResourceNotFoundException;
import com.bluehawana.rentingcarsys.model.*;
import com.bluehawana.rentingcarsys.repository.BookingRepository;
import com.bluehawana.rentingcarsys.repository.CarRepository;
import com.bluehawana.rentingcarsys.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final CarRepository carRepository;
    private final CarService carService;
    private final PaymentService paymentService;
    private final NotificationService notificationService;

    @Override
    public List<BookingResponseDTO> getBookingsByUserId(Long userId) {
        return bookingRepository.findByUser_Id(userId)
                .stream()
                .map(this::mapToBookingResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public BookingResponseDTO updateBooking(Long id, Booking updatedBooking) {
        return bookingRepository.findById(id)
                .map(booking -> {
                    booking.setStartTime(updatedBooking.getStartTime());
                    booking.setEndTime(updatedBooking.getEndTime());
                    booking.setTotalPrice(updatedBooking.getTotalPrice());
                    booking.setUpdatedAt(LocalDateTime.now());
                    return mapToBookingResponseDTO(bookingRepository.save(booking));
                })
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));
    }

    @Override
    @Transactional
    public BookingResponseDTO createBooking(Long userId, Long carId, BookingDTO bookingDTO) throws CarNotAvailableException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Car car = carRepository.findById(carId)
                .orElseThrow(() -> new ResourceNotFoundException("Car not found"));

        if (!car.isAvailable()) {
            throw new CarNotAvailableException("Car is not available for booking");
        }

        Booking booking = new Booking();
        booking.setUser(user);
        booking.setCar(car);
        booking.setStartTime(bookingDTO.getStartTime());
        booking.setEndTime(bookingDTO.getEndTime());
        booking.setStatus(BookingStatus.PENDING);
        booking.setTotalPrice(bookingDTO.getTotalPrice());
        booking.setCreatedAt(LocalDateTime.now());
        booking.setUpdatedAt(LocalDateTime.now());

        Booking savedBooking = bookingRepository.save(booking);
        return mapToBookingResponseDTO(savedBooking);
    }

    @Override
    public BookingResponseDTO mapToBookingResponseDTO(Booking booking) {
        return BookingResponseDTO.builder()
                .id(booking.getId())
                .userId(booking.getUser().getId())
                .carId(booking.getCar().getId())
                .startTime(booking.getStartTime())
                .endTime(booking.getEndTime())
                .status(booking.getStatus())
                .totalPrice(booking.getTotalPrice())
                .createdAt(booking.getCreatedAt())
                .updatedAt(booking.getUpdatedAt())
                .build();
    }

    @Override
    public List<BookingResponseDTO> getAllBookings() {
        return bookingRepository.findAll()
                .stream()
                .map(this::mapToBookingResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<BookingResponseDTO> getAllBookingsByUser() {
        // This method can be removed or implemented based on your needs
        throw new UnsupportedOperationException("Method not implemented");
    }

    @Override
    public List<BookingResponseDTO> getBookingsByUser(User user) {
        return bookingRepository.findByUser(user)
                .stream()
                .map(this::mapToBookingResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public BookingResponseDTO getBookingById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Booking ID cannot be null");
        }
        return bookingRepository.findById(id)
                .map(this::mapToBookingResponseDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));
    }

    @Override
    public void deleteBooking(Long id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));
        bookingRepository.delete(booking);
    }

    @Override
    public BookingResponseDTO updateBooking(Long id, BookingDTO bookingDTO) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));

        booking.setStartTime(bookingDTO.getStartDate());
        booking.setEndTime(bookingDTO.getEndDate());
        booking.setUpdatedAt(LocalDateTime.now());

        return mapToBookingResponseDTO(bookingRepository.save(booking));
    }

    @Override
    public void updateBookingStatus(Long id, BookingStatus status) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));

        validateStatusTransition(booking.getStatus(), status);  // Actually use the method

        booking.setStatus(status);
        booking.setUpdatedAt(LocalDateTime.now());
        bookingRepository.save(booking);

        handleStatusSpecificActions(booking, status);
    }

    private void handleStatusSpecificActions(Booking booking, BookingStatus status) {
        switch (status) {
            case CONFIRMED -> {
                carService.updateCarAvailability(booking.getCar().getId(), false);
                notificationService.sendBookingConfirmation(booking);
            }
            case CANCELLED -> {
                carService.updateCarAvailability(booking.getCar().getId(), true);
                notificationService.sendBookingCancellation(booking);
            }
            case COMPLETED -> notificationService.sendBookingCompletionConfirmation(booking);
        }
    }

    @Override
    @Transactional
    public void confirmBooking(Long id) {
        try {
            Booking booking = bookingRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));

            if (!isCarAvailableForDates(booking.getCar().getId(),
                    booking.getStartTime(),
                    booking.getEndTime())) {
                throw new CarNotAvailableException("Car is no longer available for these dates");
            }

            booking.setStatus(BookingStatus.CONFIRMED);
            booking.setUpdatedAt(LocalDateTime.now());
            booking = bookingRepository.save(booking);

            carService.updateCarAvailability(booking.getCar().getId(), false);
            notificationService.sendBookingConfirmation(booking);

        } catch (CarNotAvailableException | ResourceNotFoundException e) {
            // Re-throw these exceptions directly
            throw e;
        } catch (Exception e) {
            // Create new exception with only message
            throw new BookingConfirmationException("Failed to confirm booking: " + e.getMessage());
        }
    }

    @Override
    public void processPayment(Long bookingId, String paymentIntentId) throws PaymentException {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));

        try {
            paymentService.confirmPayment(paymentIntentId);

            booking.setStatus(BookingStatus.PAID);
            booking.setPaymentId(paymentIntentId);
            booking.setUpdatedAt(LocalDateTime.now());
            Booking savedBooking = bookingRepository.save(booking);

            // Changed from booking.getId() to savedBooking.getId()
            notificationService.sendPaymentConfirmation(savedBooking.getId());
        } catch (Exception e) {
            booking.setStatus(BookingStatus.PAYMENT_FAILED);
            bookingRepository.save(booking);
            throw new PaymentException("Payment processing failed: " + e.getMessage(), e);
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
        List<Booking> bookings = bookingRepository.findByCar_IdAndStatus(carId, BookingStatus.CONFIRMED);
        for (Booking booking : bookings) {
            if (startDate.isBefore(booking.getEndTime()) && endDate.isAfter(booking.getStartTime())) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void cancelBooking(Long id) throws PaymentException {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));

        booking.setStatus(BookingStatus.CANCELLED);
        booking.setUpdatedAt(LocalDateTime.now());
        bookingRepository.save(booking);

        carService.updateCarAvailability(booking.getCar().getId(), true);
        notificationService.sendBookingCancellation(booking);
    }

    public List<BookingResponseDTO> findBookingsByUserAndProvider(String email, ProviderType provider) {
        User user = userRepository.findByEmailAndProvider(email, String.valueOf(provider))
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        return bookingRepository.findByUser(user)
                .stream()
                .map(this::mapToBookingResponseDTO)
                .collect(Collectors.toList());
    }

    private void validateStatusTransition(BookingStatus currentStatus, BookingStatus newStatus) {
        if (currentStatus == BookingStatus.CANCELLED && newStatus != BookingStatus.CANCELLED) {
            throw new IllegalStateException("Cannot change status of a cancelled booking");
        }
        if (currentStatus == BookingStatus.COMPLETED && newStatus != BookingStatus.COMPLETED) {
            throw new IllegalStateException("Cannot change status of a completed booking");
        }
    }
}