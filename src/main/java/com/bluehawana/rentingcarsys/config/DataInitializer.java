package com.bluehawana.rentingcarsys.config;

import com.bluehawana.rentingcarsys.model.*;
import com.bluehawana.rentingcarsys.repository.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
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

            if (carRepository.count() == 0) {
                log.info("No cars found. Creating sample cars...");
                List<Car> cars = Arrays.asList(
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

                List<Car> savedCars = carRepository.saveAll(cars);
                log.info("Created {} cars", savedCars.size());

                // Print each saved car for verification
                savedCars.forEach(car ->
                        log.info("Saved car: ID={}, Model={}", car.getId(), car.getModel())
                );
            }

            // Log final car count
            log.info("Total cars in database: {}", carRepository.count());

            if (userRepository.count() == 0) {
                User testUser = new User();
                testUser.setName("Test User");
                testUser.setEmail("user@ekorental.com");
                testUser.setProvider("EMAIL");
                testUser.setRole("USER");
                User savedUser = userRepository.saveAndFlush(testUser);
                log.info("Created user with ID: {}", savedUser.getId());

                // Get a car
                List<Car> cars = carRepository.findAll();
                if (!cars.isEmpty()) {
                    Car firstCar = cars.get(0);
                    log.info("Found car with ID: {}", firstCar.getId());

                    // Create bookings
                    Booking booking1 = new Booking();
                    booking1.setUser(savedUser);
                    booking1.setCar(firstCar);
                    booking1.setStatus(String.valueOf(BookingStatus.PENDING));
                    booking1.setStartTime(LocalDateTime.now().plusDays(1));
                    booking1.setEndTime(LocalDateTime.now().plusDays(2));
                    booking1.setTotalPrice(new BigDecimal("110.00"));
                    booking1.setCreatedAt(LocalDateTime.now());
                    booking1.setUpdatedAt(LocalDateTime.now());

                    try {
                        Booking savedBooking = bookingRepository.saveAndFlush(booking1);
                        log.info("Created booking with ID: {}", savedBooking.getId());
                    } catch (Exception e) {
                        log.error("Error saving booking: ", e);
                        e.printStackTrace();
                    }

                    // Verify the save
                    bookingRepository.flush();
                    long count = bookingRepository.count();
                    log.info("Number of bookings after save: {}", count);
                }
            }
        };
    }
}