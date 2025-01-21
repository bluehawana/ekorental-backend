package com.bluehawana.rentingcarsys.service;

import com.bluehawana.rentingcarsys.dto.BookingDTO;
import com.bluehawana.rentingcarsys.dto.BookingResponseDTO;
import com.bluehawana.rentingcarsys.model.Booking;
import com.bluehawana.rentingcarsys.model.User;

import java.util.List;

public interface BookingService {
    Booking createBooking(Long userId, Long carId, BookingDTO bookingDTO);
    List<BookingResponseDTO> getAllBookingsByUser();

    List<BookingResponseDTO> getBookingsByUser(User user);

    BookingResponseDTO getBookingById(Long id);

    List<BookingResponseDTO> getAllBookings();
}