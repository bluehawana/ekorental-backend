package com.bluehawana.rentingcarsys.service;

import com.bluehawana.rentingcarsys.dto.BookingDTO;
import com.bluehawana.rentingcarsys.dto.BookingResponseDTO;
import com.bluehawana.rentingcarsys.model.Booking;
import com.bluehawana.rentingcarsys.model.BookingStatus;
import com.bluehawana.rentingcarsys.model.Car;
import com.bluehawana.rentingcarsys.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookingServiceImpl extends BookingService {

    @Autowired
    private CarService carService;

    private static final Logger log = LoggerFactory.getLogger(BookingServiceImpl.class);

    @Transactional
    @Override
    public BookingResponseDTO createBooking(Long userId, Long carId, BookingDTO bookingDTO) {
        log.info("Creating booking for user {} and car {}", userId, carId);

        Car car = carService.getCarById(carId);
        if (car == null) {
            log.error("Car with id {} not found", carId);
            throw new IllegalArgumentException("Car not found");
        }

        Booking booking = new Booking();
        User user = new User(); // Placeholder; ideally fetch from UserService
        user.setId(userId);
        booking.setUser(user);
        booking.setCarId(carId);
        booking.setStartTime(bookingDTO.getStartTime());
        booking.setEndTime(bookingDTO.getEndTime());
        booking.setTotalPrice(bookingDTO.getTotalPrice());
        booking.setStatus(String.valueOf(BookingStatus.PENDING));

        LocalDateTime now = LocalDateTime.now();
        booking.setCreatedAt(now);
        booking.setUpdatedAt(now);

        Booking savedBooking = bookingRepository.save(booking);
        log.info("Booking created with id: {}", savedBooking.getId());
        return mapToBookingResponseDTO(savedBooking);
    }

    @Override
    public BookingResponseDTO getBookingById(Long id) {
        log.info("Fetching booking with id {}", id);
        return mapToBookingResponseDTO(getBooking(id));
    }

    @Override
    public List<BookingResponseDTO> getBookingsByUserId(Long userId) {
        log.info("Fetching bookings for user id {}", userId);
        return bookingRepository.findByUserId(userId).stream()
                .map(this::mapToBookingResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public BookingResponseDTO updateBooking(Long id, Booking updatedBooking) {
        log.info("Updating booking with id {}", id);
        Booking booking = getBooking(id);
        booking.setStartTime(updatedBooking.getStartTime());
        booking.setEndTime(updatedBooking.getEndTime());
        booking.setTotalPrice(updatedBooking.getTotalPrice());
        booking.setStatus(updatedBooking.getStatus());
        booking.setUpdatedAt(LocalDateTime.now());
        return mapToBookingResponseDTO(bookingRepository.save(booking));
    }

    @Override
    public Booking updateBookingStatus(Long id, BookingStatus status) {
        log.info("Updating booking {} status to {}", id, status);
        Booking booking = getBooking(id);
        booking.setStatus(String.valueOf(status));
        booking.setUpdatedAt(LocalDateTime.now());
        return bookingRepository.save(booking);
    }

    @Transactional
    @Override
    public void confirmBooking(Long id) {
        log.info("Confirming booking with id {}", id);
        Booking booking = getBooking(id);
        if (!BookingStatus.PENDING.equals(booking.getStatus())) {
            log.warn("Booking {} cannot be confirmed; current status is {}", id, booking.getStatus());
            throw new IllegalStateException("Booking must be PENDING to confirm");
        }
        booking.setStatus(String.valueOf(BookingStatus.CONFIRMED));
        booking.setUpdatedAt(LocalDateTime.now());
        bookingRepository.save(booking);
        log.info("Booking {} confirmed", id);
    }

    @Override
    public void processPayment(Long bookingId, String paymentIntentId) {
        log.info("Processing payment for booking {} with paymentIntentId {}", bookingId, paymentIntentId);
        confirmBooking(bookingId);
    }

    @Override
    public void cancelBooking(Long id) {
        log.info("Cancelling booking with id {}", id);
        Booking booking = getBooking(id);
        booking.setStatus(String.valueOf(BookingStatus.CANCELLED));
        booking.setUpdatedAt(LocalDateTime.now());
        bookingRepository.save(booking);
        log.info("Booking {} cancelled", id);
    }

    @Override
    public boolean isCarAvailableForDates(Long carId, LocalDateTime startDate, LocalDateTime endDate) {
        log.info("Checking availability for car {} from {} to {}", carId, startDate, endDate);
        return true; // Unlimited cars, always available
    }

    @Override
    public void sendBookingConfirmation(Long bookingId) {
        log.info("Sending confirmation for booking {}", bookingId);
        // No-op for simplicity
    }

    @Transactional
    @Override
    public BookingDTO saveBooking(BookingDTO bookingDTO) {
        log.info("Saving booking: {}", bookingDTO);
        Booking booking = bookingDTO.toEntity();
        if (booking.getStatus() == null) {
            booking.setStatus(String.valueOf(BookingStatus.PENDING));
        }
        if (booking.getCreatedAt() == null) {
            booking.setCreatedAt(LocalDateTime.now());
        }
        booking.setUpdatedAt(LocalDateTime.now());
        Booking savedBooking = bookingRepository.save(booking);
        log.info("Booking saved with id: {}", savedBooking.getId());
        return BookingDTO.fromEntity(savedBooking);
    }

    @Override
    public List<BookingResponseDTO> getAllBookingsByUser() {
        log.info("Fetching all bookings for current user");
        return getAllBookings(); // Simple for homework
    }

    @Override
    public List<BookingResponseDTO> getBookingsByUser(User user) {
        log.info("Fetching bookings for user with id {}", user.getId());
        return bookingRepository.findByUserId(user.getId()).stream()
                .map(this::mapToBookingResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Booking updateBookingStatusAfterPayment(Long id) {
        log.info("Updating booking status after payment for id {}", id);
        return updateBookingStatus(id, BookingStatus.CONFIRMED);
    }
}