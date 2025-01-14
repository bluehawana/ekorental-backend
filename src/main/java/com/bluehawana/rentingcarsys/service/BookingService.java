package com.bluehawana.rentingcarsys.service;

import com.bluehawana.rentingcarsys.model.Booking;
import com.bluehawana.rentingcarsys.model.BookingStatus;
import com.bluehawana.rentingcarsys.model.Car;
import com.bluehawana.rentingcarsys.model.User;
import com.bluehawana.rentingcarsys.repository.BookingRepository;
import com.bluehawana.rentingcarsys.repository.CarRepository;
import com.bluehawana.rentingcarsys.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public abstract class BookingService {

    private final BookingRepository bookingRepository;
    private final CarRepository carRepository;
    private final UserRepository userRepository;

    // Constructor for dependency injection
    public BookingService(BookingRepository bookingRepository, CarRepository carRepository, UserRepository userRepository) {
        this.bookingRepository = bookingRepository;
        this.carRepository = carRepository;
        this.userRepository = userRepository;
    }

    public BookingService() {
        this.bookingRepository = null;
        this.carRepository = null;
        this.userRepository = null;
    }

    // Method to create a booking
    public Booking createBooking(Booking booking) {
        // Validate booking dates
        if (booking.getStartDate().isAfter(booking.getEndDate())) {
            throw new IllegalArgumentException("Start date must be before end date");
        }

        // Validate user
        User user = userRepository.findById(booking.getUser().getId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        // Check car availability
        Car car = booking.getCar();
        if (!car.getIsAvailable()) {
            throw new IllegalStateException("Car is not available");
        }

        // Save booking
        booking.setUser(user);
        booking.setStatus(BookingStatus.PENDING);
        Booking savedBooking = bookingRepository.save(booking);

        // Update car availability
        car.setIsAvailable(false);
        carRepository.save(car);

        return savedBooking;
    }

    public abstract Booking createBooking(Booking booking, User currentUser);

    public abstract Booking getBooking(Long id, User currentUser);

    public abstract List<Booking> getUserBookings(User currentUser);

    public abstract Booking updateBooking(Long id, Booking bookingDetails, User currentUser);

    // Method to delete a booking (implementation)
    public void deleteBooking(Long id, User currentUser) {
        // Find booking by id
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Booking not found"));

        // Validate that the current user can delete this booking
        if (!booking.getUser().equals(currentUser)) {
            throw new IllegalStateException("You are not authorized to delete this booking");
        }

        // Mark the car as available again
        Car car = booking.getCar();
        car.setIsAvailable(true);
        carRepository.save(car);

        // Delete the booking
        bookingRepository.delete(booking);
    }
}

