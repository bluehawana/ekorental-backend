package com.bluehawana.rentingcarsys.service;

import com.bluehawana.rentingcarsys.dto.BookingDTO;
import com.bluehawana.rentingcarsys.dto.BookingResponseDTO;
import com.bluehawana.rentingcarsys.model.Booking;
import com.bluehawana.rentingcarsys.model.BookingStatus;
import com.bluehawana.rentingcarsys.model.User;
import com.bluehawana.rentingcarsys.repository.BookingRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public abstract class BookingService {

    @Autowired
    protected BookingRepository bookingRepository; // Changed to protected for subclass access

    public Booking getBooking(Long id) {
        return bookingRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Booking not found with id: " + id));
    }

    @Transactional
    public abstract BookingResponseDTO createBooking(Long userId, Long carId, BookingDTO bookingDTO);

    // Changed from private to protected to allow overriding
    protected BookingResponseDTO mapToBookingResponseDTO(Booking save) {
        return BookingResponseDTO.builder()
                .id(save.getId())
                .userId(save.getUser().getId())
                .carId(save.getCarId())
                .startTime(save.getStartTime())
                .endTime(save.getEndTime())
                .status(BookingStatus.valueOf(save.getStatus()))
                .totalPrice(save.getTotalPrice())
                .createdAt(save.getCreatedAt())
                .updatedAt(save.getUpdatedAt())
                .build();
    }

    public abstract Booking updateBookingStatusAfterPayment(Long id);

    public Booking updateBookingStatus(Long id, String status) {
        log.info("Updating booking {} status to {}", id, status);
        Booking booking = getBooking(id);
        booking.setStatus(String.valueOf(BookingStatus.valueOf(status)));
        return bookingRepository.save(booking);
    }

    public Booking createBooking(
            Long carId,
            LocalDateTime startTime,
            LocalDateTime endTime,
            Integer totalHours,
            BigDecimal totalPrice,
            User user,
            String status) {

        Booking booking = new Booking();
        booking.setCarId(carId);
        booking.setStartTime(startTime);
        booking.setEndTime(endTime);
        booking.setTotalHours(totalHours);
        booking.setTotalPrice(totalPrice);
        booking.setUser(user);
        booking.setStatus(String.valueOf(BookingStatus.valueOf(status)));

        return bookingRepository.save(booking);
    }

    public abstract BookingResponseDTO getBookingById(Long id);

    public abstract List<BookingResponseDTO> getBookingsByUserId(Long userId);

    public abstract BookingResponseDTO updateBooking(Long id, Booking updatedBooking);

    public abstract Booking updateBookingStatus(Long id, BookingStatus status);

    // Changed from static to non-static
    @Transactional
    public abstract void confirmBooking(Long id);

    public abstract void processPayment(Long bookingId, String paymentIntentId);

    public abstract void cancelBooking(Long id);

    public abstract boolean isCarAvailableForDates(Long carId, LocalDateTime startDate, LocalDateTime endDate);

    public abstract void sendBookingConfirmation(Long bookingId);

    public BookingResponseDTO updateBooking(Long id, BookingDTO bookingDTO) {
        log.info("Updating booking with id: {}", id);
        Booking booking = getBooking(id);
        booking.setStartTime(bookingDTO.getStartTime());
        booking.setEndTime(bookingDTO.getEndTime());
        booking.setTotalPrice(bookingDTO.getTotalPrice());
        booking.setStatus(String.valueOf(BookingStatus.valueOf(bookingDTO.getStatus())));
        booking.setUpdatedAt(LocalDateTime.now());
        return mapToBookingResponseDTO(bookingRepository.save(booking));
    }

    @Transactional
    public abstract BookingDTO saveBooking(BookingDTO bookingDTO);

    public List<BookingResponseDTO> getAllBookings() {
        return bookingRepository.findAll().stream()
                .map(this::mapToBookingResponseDTO)
                .collect(Collectors.toList());
    }

    public abstract List<BookingResponseDTO> getAllBookingsByUser();

    public abstract List<BookingResponseDTO> getBookingsByUser(User user);

    public void deleteBooking(Long id) {
        log.info("Deleting booking with id: {}", id);
        bookingRepository.deleteById(id);
    }
}