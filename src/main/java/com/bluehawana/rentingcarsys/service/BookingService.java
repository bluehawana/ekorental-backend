package com.bluehawana.rentingcarsys.service;

import com.bluehawana.rentingcarsys.model.Booking;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface BookingService {
    Booking createBooking(Long carId, LocalDateTime startTime, LocalDateTime endTime,
                          Integer totalHours, BigDecimal totalPrice, String userEmail,
                          String userName, String status);

    Booking getBookingById(Long id);
    List<Booking> getBookingsByCar_Id(Long carId);
    List<Booking> getBookingsByUserEmail(String userEmail);
    Booking updateBookingStatus(Long id, String status);
    void deleteBooking(Long id);
}