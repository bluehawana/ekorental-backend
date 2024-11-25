package com.bluehawana.rentingcarsys.service;


import com.bluehawana.rentingcarsys.exception.ResourceNotFoundException;
import com.bluehawana.rentingcarsys.model.Booking;
import com.bluehawana.rentingcarsys.model.Car;
import com.bluehawana.rentingcarsys.model.BookingStatus;
import com.bluehawana.rentingcarsys.repository.BookingRepository;
import com.bluehawana.rentingcarsys.repository.CarRepository;
import com.bluehawana.rentingcarsys.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.math.BigDecimal;
import java.util.List;

@Service
public class BookingServiceImpl implements BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private CarRepository carRepository;

    @Override
    public Booking createBooking(Long carId, LocalDateTime startTime, LocalDateTime endTime,
                                 Integer totalHours, BigDecimal totalPrice, String userEmail,
                                 String userName, String status) {
        Car car = carRepository.findById(carId)
                .orElseThrow(() -> new ResourceNotFoundException("Car not found"));

        Booking booking = new Booking();
        booking.setCar(car);
        booking.setStartTime(startTime);
        booking.setEndTime(endTime);
        booking.setTotalHours(totalHours);
        booking.setTotalPrice(totalPrice);
        booking.setUserEmail(userEmail);
        booking.setUserName(userName);
        booking.setStatus(BookingStatus.valueOf(status));

        return bookingRepository.save(booking);
    }

    @Override
    public Booking getBookingById(Long id) {
        return bookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));
    }

    @Override
    public List<Booking> getBookingsByCar_Id(Long carId) {
        return bookingRepository.findByCar_Id(carId);
    }

    @Override
    public List<Booking> getBookingsByUserEmail(String userEmail) {
        return bookingRepository.findByUserEmail(userEmail);
    }

    @Override
    public Booking updateBookingStatus(Long id, String status) {
        Booking booking = getBookingById(id);
        booking.setStatus(BookingStatus.valueOf(status));
        return bookingRepository.save(booking);
    }

    @Override
    public void deleteBooking(Long id) {
        bookingRepository.deleteById(id);
    }
}