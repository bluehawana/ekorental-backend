package com.bluehawana.rentingcarsys.service;

import com.bluehawana.rentingcarsys.model.Booking;
import java.util.List;

public interface BookingService {
    Booking createBooking(Booking booking);
    Booking getBookingById(Long id);
    List<Booking> getAllBookings();
    Booking updateBooking(Booking booking);
    void deleteBooking(Long id);
}
