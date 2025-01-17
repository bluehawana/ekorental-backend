package com.bluehawana.rentingcarsys.service;

import com.bluehawana.rentingcarsys.dto.BookingDTO;
import com.bluehawana.rentingcarsys.dto.BookingResponseDTO;
import com.bluehawana.rentingcarsys.model.Booking;

import java.util.List;

public interface BookingService {
    Booking createBooking(BookingDTO bookingDTO);
    List<BookingResponseDTO> getAllBookings();
    List<BookingResponseDTO> getUserBookings(Long userId);
    BookingResponseDTO getBookingById(Long id);
    Booking updateBooking(Long id, BookingDTO bookingDTO);
    void deleteBooking(Long id);
}
