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
    // Existing methods
    BookingResponseDTO createBooking(Long userId, Long carId, BookingDTO bookingDTO);
    List<BookingResponseDTO> getAllBookingsByUser();
    List<BookingResponseDTO> getBookingsByUser(User user);
    BookingResponseDTO getBookingById(Long id);
    List<BookingResponseDTO> getAllBookings();
    List<BookingResponseDTO> getBookingsByUserId(Long userId);
    void deleteBooking(Long id);
    BookingResponseDTO updateBooking(Long id, BookingDTO bookingDTO);
    BookingResponseDTO updateBooking(Long id, Booking bookingDTO);

    // New methods for enhanced functionality
    void updateBookingStatus(Long id, BookingStatus status);
    void confirmBooking(Long id) throws BookingConfirmationException, CarNotAvailableException;
    void processPayment(Long bookingId, String paymentIntentId) throws PaymentException;
    void sendBookingConfirmation(Long bookingId);
    boolean isCarAvailableForDates(Long carId, LocalDateTime startDate, LocalDateTime endDate);
    void cancelBooking(Long id) throws PaymentException;
}