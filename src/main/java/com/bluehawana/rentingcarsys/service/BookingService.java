package com.bluehawana.rentingcarsys.service;

import com.bluehawana.rentingcarsys.dto.BookingDTO;
import com.bluehawana.rentingcarsys.dto.BookingResponseDTO;
import com.bluehawana.rentingcarsys.model.Booking;
import com.bluehawana.rentingcarsys.model.BookingStatus;
import com.bluehawana.rentingcarsys.model.User;
import com.bluehawana.rentingcarsys.model.Car;
import com.bluehawana.rentingcarsys.repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public abstract class BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    public Booking createBooking(BookingDTO bookingDTO, User user, Car car) {
        Booking booking = new Booking();
        booking.setUser(user);
        booking.setCar(car);
        booking.setStartTime(bookingDTO.getStartTime());
        booking.setEndTime(bookingDTO.getEndTime());
        booking.setTotalPrice(bookingDTO.getTotalPrice());
        booking.setStatus(BookingStatus.CONFIRMED); // Set the default status here

        return bookingRepository.save(booking);
    }

    public Booking createBooking(BookingDTO bookingDTO) {
        // Implementation to get user and car from the database
        User user = new User();
        Car car = new Car();
        return createBooking(bookingDTO, user, car);
    }

    public List<BookingResponseDTO> getAllBookings() {
        List<Booking> bookings = bookingRepository.findAll();
        // Convert Booking to BookingResponseDTO if necessary
        return bookings.stream()
                       .map(BookingResponseDTO::new)
                       .collect(Collectors.toList());
    }

    public abstract List<Booking> getUserBookings(Long userId);

    public abstract Booking getBookingById(Long id);

    public abstract Booking updateBooking(Long id, BookingDTO bookingDTO);

    public abstract void deleteBooking(Long id);
}