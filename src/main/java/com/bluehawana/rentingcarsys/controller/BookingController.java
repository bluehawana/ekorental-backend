package com.bluehawana.rentingcarsys.controller;

import com.bluehawana.rentingcarsys.dto.BookingRequest;
import com.bluehawana.rentingcarsys.model.*;
import com.bluehawana.rentingcarsys.repository.CarRepository;
import com.bluehawana.rentingcarsys.repository.UserRepository;
import com.bluehawana.rentingcarsys.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/bookings")
@CrossOrigin(origins = "http://localhost:3000")
public class BookingController {

    private final BookingService bookingService;
    private final CarRepository carRepository;
    private final UserRepository userRepository;

    @Autowired
    public BookingController(BookingService bookingService,
                             CarRepository carRepository,
                             UserRepository userRepository) {
        this.bookingService = bookingService;
        this.carRepository = carRepository;
        this.userRepository = userRepository;
    }

    @PostMapping
    public ResponseEntity<Booking> createBooking(@RequestBody BookingRequest request) {
        try {
            // Find car and user
            Car car = carRepository.findById(request.getCarId())
                    .orElseThrow(() -> new RuntimeException("Car not found"));

            User user = userRepository.findById(request.getUserId())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            // Create booking
            Booking booking = Booking.builder()
                    .car(car)
                    .user(user)
                    .startDate(request.getStartDate())
                    .endDate(request.getEndDate())
                    .totalPrice(request.getTotalPrice())
                    .status(BookingStatus.PENDING)
                    .build();

            Booking savedBooking = bookingService.createBooking(booking);
            return ResponseEntity.ok(savedBooking);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}