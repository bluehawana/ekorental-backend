package com.bluehawana.rentingcarsys.config;

import com.bluehawana.rentingcarsys.model.*;
import com.bluehawana.rentingcarsys.repository.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Configuration
@Slf4j
public class DataInitializer {

    @Bean
    CommandLineRunner initDatabase(
            CarRepository carRepository,
            UserRepository userRepository,
            BookingRepository bookingRepository) {
        return args -> {
            log.info("Starting database initialization check...");

            List<Car> savedCars = null;

            // Create cars only if cars table is empty
            if (carRepository.count() == 0) {
                log.info("No cars found. Creating sample cars...");
                List<Car> cars = List.of(
                        Car.builder()
                                .model("Tesla Model 3")
                                .licensePlate("ABC123")
                                .hourRate(new BigDecimal("110.00"))
                                .imageUrl("http://localhost:8080/uploads/model_3.png")
                                .isAvailable(true)
                                .location("Chalmers University")
                                .year(2024)
                                .description("Model 3: Most economical choice. Perfect for daily commute.")
                                .build(),
                        Car.builder()
                                .model("Tesla Model Y")
                                .licensePlate("DEF456")
                                .hourRate(new BigDecimal("120.00"))
                                .imageUrl("http://localhost:8080/uploads/model_y.png")
                                .isAvailable(true)
                                .location("Lindholmen Science Park")
                                .year(2024)
                                .description("Model Y: Practical family choice with great efficiency.")
                                .build(),
                        Car.builder()
                                .model("Tesla Model S")
                                .licensePlate("GHI789")
                                .hourRate(new BigDecimal("150.00"))
                                .imageUrl("http://localhost:8080/uploads/model_s.png")
                                .isAvailable(true)
                                .location("Lindholmen Science Park")
                                .year(2024)
                                .description("Model S: Luxury sedan with impressive range.")
                                .build(),
                        Car.builder()
                                .model("Tesla Model X")
                                .licensePlate("JKL012")
                                .hourRate(new BigDecimal("170.00"))
                                .imageUrl("http://localhost:8080/uploads/model_x.png")
                                .isAvailable(true)
                                .location("Nordstan Park")
                                .year(2024)
                                .description("Model X: Spacious SUV perfect for group travel.")
                                .build(),
                        Car.builder()
                                .model("Tesla Cybertruck")
                                .licensePlate("MNO345")
                                .hourRate(new BigDecimal("250.00"))
                                .imageUrl("http://localhost:8080/uploads/cybertruck.png")
                                .isAvailable(true)
                                .location("Chalmers University")
                                .year(2024)
                                .description("Cybertruck: Future of electric utility vehicles.")
                                .build(),
                        Car.builder()
                                .model("Tesla Roadster")
                                .licensePlate("PQR678")
                                .hourRate(new BigDecimal("300.00"))
                                .imageUrl("http://localhost:8080/uploads/roadster.png")
                                .isAvailable(true)
                                .location("Nordstan Park")
                                .year(2024)
                                .description("Roadster: High-performance sports car.")
                                .build()
                );
               savedCars = carRepository.saveAll(cars);
                log.info("Created {} cars", savedCars.size());
                log.info("Sample cars created successfully");
            } else {
                log.info("Cars already exist in database");
            }

            // First get or create the test user
            if (userRepository.count() == 0) {
                User testUser1 = userRepository.save(User.builder()
                        .name("Test User")
                        .email("user@ekorental.com")
                        .avatarUrl("https://api.dicebear.com/7.x/avataaars/svg?seed=TestUser")
                        .provider(AuthProvider.EMAIL)
                        .role(UserRole.USER)
                        .build());

                User adminUser = userRepository.save(User.builder()
                        .name("Admin User")
                        .email("admin@ekorental.com")
                        .avatarUrl("https://api.dicebear.com/7.x/avataaars/svg?seed=Admin")
                        .provider(AuthProvider.EMAIL)
                        .role(UserRole.ADMIN)
                        .build());

                log.info("Users created successfully");

                // Only create bookings if there are no bookings AND we just created users
                if (bookingRepository.count() == 0 && savedCars != null && !savedCars.isEmpty()) {
                    Car firstCar = savedCars.get(0);
                    log.info("Creating bookings for car: {} (ID: {})", firstCar.getModel(), firstCar.getId());
                    try {
                        // Past booking (completed)
                        Booking pastBooking = bookingRepository.save(Booking.builder()
                                .user(testUser1)
                                .car(firstCar)
                                .status(BookingStatus.COMPLETED)
                                .startTime(LocalDateTime.now().minusDays(7))
                                .endTime(LocalDateTime.now().minusDays(6))
                                .totalPrice(new BigDecimal("220.00"))
                                .build());
                        log.info("Created past booking with ID: {}", pastBooking.getId());
                        // Current active booking
                        Booking currentBooking = bookingRepository.save(Booking.builder()
                                .user(testUser1)
                                .car(firstCar)
                                .status(BookingStatus.CONFIRMED)
                                .startTime(LocalDateTime.now().minusHours(2))
                                .endTime(LocalDateTime.now().plusHours(2))
                                .totalPrice(new BigDecimal("110.00"))
                                .build());
                        log.info("Created current booking with ID: {}", currentBooking.getId());
                        // Future booking (pending)
                        Booking futureBooking = bookingRepository.save(Booking.builder()
                                .user(testUser1)
                                .car(firstCar)
                                .status(BookingStatus.PENDING)
                                .startTime(LocalDateTime.now().plusDays(1))
                                .endTime(LocalDateTime.now().plusDays(2))
                                .totalPrice(new BigDecimal("440.00"))
                                .build());
                        log.info("Created future booking with ID: {}", futureBooking.getId());
                        // Cancelled booking
                        Booking cancelledBooking = bookingRepository.save(Booking.builder()
                                .user(testUser1)
                                .car(firstCar)
                                .status(BookingStatus.CANCELLED)
                                .startTime(LocalDateTime.now().plusDays(5))
                                .endTime(LocalDateTime.now().plusDays(6))
                                .totalPrice(new BigDecimal("330.00"))
                                .build());
                        log.info("Created cancelled booking with ID: {}", cancelledBooking.getId());
                        log.info("Successfully created all test bookings");
                    } catch (Exception e) {
                        log.error("Error creating bookings: {}", e.getMessage());
                    }
                } else {
                    log.error("No cars available to create bookings");
                }
            } else {
                log.info("Users already exist in database");
            }
            // Log final counts
            log.info("Initialized database with:");
            log.info(" - {} cars", carRepository.count());
            log.info(" - {} users", userRepository.count());
            log.info(" - {} bookings", bookingRepository.count());
        };
    }
}