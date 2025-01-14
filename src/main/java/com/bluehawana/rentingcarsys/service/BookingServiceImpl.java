package com.bluehawana.rentingcarsys.service;

import com.bluehawana.rentingcarsys.model.Booking;
import com.bluehawana.rentingcarsys.model.BookingStatus;
import com.bluehawana.rentingcarsys.model.Car;
import com.bluehawana.rentingcarsys.model.User;
import com.bluehawana.rentingcarsys.repository.BookingRepository;
import com.bluehawana.rentingcarsys.repository.CarRepository;
import com.bluehawana.rentingcarsys.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookingServiceImpl extends BookingService {

    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final CarRepository carRepository;

    public BookingServiceImpl() {
        this.bookingRepository = null;
        this.userRepository = null;
        this.carRepository = null;
    }

    @Autowired
    public BookingServiceImpl(BookingRepository bookingRepository, UserRepository userRepository, CarRepository carRepository) {
        super();
        this.bookingRepository = bookingRepository;
        this.userRepository = userRepository;
        this.carRepository = carRepository;
    }

    @Override
    public Booking createBooking(Booking booking, User currentUser) {
        User user = userRepository.findByEmail(currentUser.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Car car = carRepository.findById(booking.getCar().getId())
                .orElseThrow(() -> new RuntimeException("Car not found"));

        booking.setUser(user);
        booking.setCar(car);
        booking.setStatus(BookingStatus.PENDING);

        return bookingRepository.save(booking);
    }

    @Override
    public Booking getBooking(Long id, User currentUser) {
        return bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found"));
    }

    @Override
    public List<Booking> getUserBookings(User currentUser) {
        return bookingRepository.findByUser(currentUser);
    }

    @Override
    public Booking updateBooking(Long id, Booking bookingDetails, User currentUser) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        booking.setStartDate(bookingDetails.getStartDate());
        booking.setEndDate(bookingDetails.getEndDate());
        booking.setTotalPrice(bookingDetails.getTotalPrice());
        booking.setStatus(bookingDetails.getStatus());

        return bookingRepository.save(booking);
    }

    @Override
    public void deleteBooking(Long id, User currentUser) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found"));
        bookingRepository.delete(booking);
    }

    public void createBooking() {
    }

    public void getBookingById() {
    }

    public void getBookingByCarLicensePlate() {
    }

    public void getAllBookings() {
    }

    public void updateBooking() {
    }

    public void deleteBooking() {
    }
}