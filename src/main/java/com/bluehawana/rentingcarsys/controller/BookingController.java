package com.bluehawana.rentingcarsys.controller;

import com.bluehawana.rentingcarsys.dto.BookingDTO;
import com.bluehawana.rentingcarsys.dto.BookingResponseDTO;
import com.bluehawana.rentingcarsys.exception.BookingConfirmationException;
import com.bluehawana.rentingcarsys.exception.CarNotAvailableException;
import com.bluehawana.rentingcarsys.exception.ErrorResponse;
import com.bluehawana.rentingcarsys.model.BookingStatus;
import com.bluehawana.rentingcarsys.model.ProviderType;
import com.bluehawana.rentingcarsys.model.User;
import com.bluehawana.rentingcarsys.service.BookingService;
import com.bluehawana.rentingcarsys.service.CarService;
import com.bluehawana.rentingcarsys.service.NotificationService;
import com.bluehawana.rentingcarsys.util.SecurityUtils;
import com.bluehawana.rentingcarsys.repository.UserRepository;
import com.google.common.io.Files;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class BookingController {

    static final Logger log = LoggerFactory.getLogger(BookingController.class);

    private final BookingService bookingService;
    private final CarService carService;
    private final NotificationService notificationService;
    private final UserRepository userRepository;

    // After
    @PostMapping("/bookings")
    public ResponseEntity<?> createBooking(@RequestBody BookingDTO bookingDTO) throws CarNotAvailableException {
        log.info("Received booking request: {}", bookingDTO);

        try {
            User user = userRepository.findById(SecurityUtils.getCurrentUserId())
                    .orElseThrow(() -> new RuntimeException("User not found"));
            BookingResponseDTO booking = bookingService.createBooking(
                    user.getId(),
                    bookingDTO.getCarId(),
                    bookingDTO
            );

            log.info("Booking created successfully: {}", booking);
            return ResponseEntity.status(HttpStatus.CREATED).body(booking);
        } catch (Exception e) {
            log.error("Error creating booking: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse(e.getMessage()));
        }
    }

    @GetMapping("/bookings")
    public ResponseEntity<List<BookingResponseDTO>> getAllBookings() {
        List<BookingResponseDTO> bookings = bookingService.getAllBookings();
        return ResponseEntity.ok(bookings);
    }

    @GetMapping("/bookings/{id}")
    public ResponseEntity<BookingResponseDTO> getBookingById(@PathVariable Long id) {
        try {
            BookingResponseDTO booking = bookingService.getBookingById(id);
            return ResponseEntity.ok(booking);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(null);
        }
    }

    @GetMapping("bookings/user/{userId}")
    public ResponseEntity<List<Object>> getBookingsByUserId(@PathVariable Long userId) {
        List<Object> bookings = Collections.singletonList(bookingService.getBookingsByUserId(userId));
        if (bookings.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(bookings);
        }
        return ResponseEntity.ok(bookings);
    }

    @DeleteMapping("/bookings/{id}")
    public ResponseEntity<?> deleteBooking(@PathVariable Long id) {
        try {
            bookingService.deleteBooking(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("{\"error\": \"" + e.getMessage() + "\"}");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"error\": \"Error deleting booking\"}");
        }
    }

    @PutMapping("/bookings/{id}")
    public BookingResponseDTO updateBooking(@PathVariable Long id, @RequestBody BookingDTO updatedBooking) {
        return bookingService.updateBooking(id, updatedBooking);
    }

    @PostMapping("/bookings/{id}/confirm")
    public ResponseEntity<?> confirmBooking(@PathVariable Long id) {
        try {
            bookingService.confirmBooking(id);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("{\"error\": \"" + e.getMessage() + "\"}");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"error\": \"Error confirming booking\"}");
        }
    }

    @PostMapping("/cars/{id}/availability")
    public ResponseEntity<?> updateCarAvailability(@PathVariable Long id, @RequestParam boolean available) {
        try {
            carService.updateCarAvailability(id, available);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("{\"error\": \"" + e.getMessage() + "\"}");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"error\": \"Error updating car availability\"}");
        }
    }

    @PostMapping("/notifications/email")
    public ResponseEntity<?> sendEmailConfirmation(@RequestParam Long bookingId) {
        try {
            notificationService.sendPaymentConfirmation(bookingId);  // Changed from sendEmailConfirmation
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"error\": \"Error sending email confirmation\"}");
        }
    }
}