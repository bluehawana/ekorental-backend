package com.bluehawana.rentingcarsys.controller;

import com.bluehawana.rentingcarsys.dto.BookingRequestDTO;
import com.bluehawana.rentingcarsys.dto.CustomErrorResponse;
import com.bluehawana.rentingcarsys.model.Booking;
import com.bluehawana.rentingcarsys.service.BookingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    // Create booking
    @PostMapping("/cars/{carId}/book")
    public ResponseEntity<?> createBooking(
            @PathVariable Long carId,
            @RequestBody BookingRequestDTO request) {
        try {
            Booking booking = bookingService.createBooking(
                    carId,
                    request.getStartTime(),
                    request.getEndTime(),
                    request.getTotalHours(),
                    request.getTotalPrice(),
                    (String) request.getUserEmail(),
                    request.getUserName(),
                    request.getStatus()
            );
            return ResponseEntity.ok(booking);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new CustomErrorResponse(e.getMessage()));
        }
    }

    // Get all bookings for a car
    @GetMapping("/cars/{carId}/bookings")
    public ResponseEntity<List<Booking>> getBookingsForCar(@PathVariable Long carId) {
        List<Booking> bookings = bookingService.getBookingsByCar_Id(carId);
        return ResponseEntity.ok(bookings);
    }

    // Get booking by ID
    @GetMapping("/bookings/{id}")
    public ResponseEntity<Booking> getBooking(@PathVariable Long id) {
        return ResponseEntity.ok(bookingService.getBookingById(id));
    }

    // Get user's bookings
    @GetMapping("/bookings/user/{email}")
    public ResponseEntity<List<Booking>> getUserBookings(@PathVariable String email) {
        List<Booking> bookings = bookingService.getBookingsByUserEmail(email);
        return ResponseEntity.ok(bookings);
    }

    // Update booking status
    @PutMapping("/bookings/{id}/status")
    public ResponseEntity<Booking> updateBookingStatus(
            @PathVariable Long id,
            @RequestBody String status) {
        return ResponseEntity.ok(bookingService.updateBookingStatus(id, status));
    }

    // Delete booking
    @DeleteMapping("/bookings/{id}")
    public ResponseEntity<Void> deleteBooking(@PathVariable Long id) {
        bookingService.deleteBooking(id);
        return ResponseEntity.ok().build();
    }
}
