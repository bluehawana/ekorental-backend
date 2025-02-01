package com.bluehawana.rentingcarsys.service;

import com.bluehawana.rentingcarsys.dto.BookingDTO;
import com.bluehawana.rentingcarsys.dto.BookingResponseDTO;
import com.bluehawana.rentingcarsys.exception.BookingConfirmationException;
import com.bluehawana.rentingcarsys.exception.CarNotAvailableException;
import com.bluehawana.rentingcarsys.exception.PaymentException;
import com.bluehawana.rentingcarsys.model.Booking;
import com.bluehawana.rentingcarsys.model.BookingStatus;
import com.bluehawana.rentingcarsys.model.User;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingService {
    // Core booking operations
    BookingResponseDTO createBooking(Long userId, Long carId, BookingDTO bookingDTO) throws CarNotAvailableException;
    BookingResponseDTO getBookingById(Long id);
    List<BookingResponseDTO> getAllBookings();
    void deleteBooking(Long id);
    BookingResponseDTO updateBooking(Long id, BookingDTO bookingDTO);

    // User-specific booking operations
    List<BookingResponseDTO> getAllBookingsByUser();
    List<BookingResponseDTO> getBookingsByUser(User user);
    List<BookingResponseDTO> getBookingsByUserId(Long userId);

    BookingResponseDTO updateBooking(Long id, Booking updatedBooking);

    // Status and payment management
    void updateBookingStatus(Long id, BookingStatus status);
    void confirmBooking(Long id) throws BookingConfirmationException, CarNotAvailableException;
    void processPayment(Long bookingId, String paymentIntentId) throws PaymentException;
    void cancelBooking(Long id) throws PaymentException;

    // Availability checks and notifications
    boolean isCarAvailableForDates(Long carId, LocalDateTime startDate, LocalDateTime endDate);
    void sendBookingConfirmation(Long bookingId);

    // DTO mapping
    BookingResponseDTO mapToBookingResponseDTO(Booking booking);
}