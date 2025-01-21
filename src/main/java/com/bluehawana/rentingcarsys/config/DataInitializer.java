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
import java.util.Optional;

@Configuration
@Slf4j
public class DataInitializer {

    @Bean
    CommandLineRunner initDatabase(
            CarRepository carRepository,
            UserRepository userRepository,
            BookingRepository bookingRepository) {
        return args -> {
            log.info("Starting database initialization...");

            // Check if cars table is empty
            if (carRepository.count() == 0) {
                // Create cars if no cars exist
                List<Car> cars = List.of(
                        // ... existing car data ...
                );

                List<Car> savedCars = carRepository.saveAll(cars);
                log.info("Created {} cars", savedCars.size());
            } else {
                log.info("Cars already exist in the database. Skipping car initialization.");
            }

            // Check if users table is empty
            if (userRepository.count() == 0) {
                // Create test user if no users exist
                User testUser1 = createOrUpdateUser(userRepository, User.builder()
                        .name("Test User")
                        .email("user@ekorental.com")
                        .avatarUrl("https://api.dicebear.com/7.x/avataaars/svg?seed=TestUser")
                        .provider(AuthProvider.EMAIL)
                        .role(UserRole.USER)
                        .build());

                // Create admin user if no admin exists
                User adminUser = createOrUpdateUser(userRepository, User.builder()
                        .name("Admin User")
                        .email("admin@ekorental.com")
                        .avatarUrl("https://api.dicebear.com/7.x/avataaars/svg?seed=Admin")
                        .provider(AuthProvider.EMAIL)
                        .role(UserRole.ADMIN)
                        .build());
            } else {
                log.info("Users already exist in the database. Skipping user initialization.");
            }

            // Check if bookings table is empty
            if (bookingRepository.count() == 0) {
                // Create sample bookings if no bookings exist
                List<Car> cars = carRepository.findAll();
                List<User> users = userRepository.findAll();

                if (!cars.isEmpty() && !users.isEmpty()) {
                    Car firstCar = cars.get(0);
                    User firstUser = users.get(0);

                    // ... create sample bookings using firstCar and firstUser ...

                    log.info("Successfully created all test bookings");
                } else {
                    log.warn("No cars or users available to create sample bookings.");
                }
            } else {
                log.info("Bookings already exist in the database. Skipping booking initialization.");
            }

            // Log final counts
            log.info("Database initialization completed.");
            log.info("Current database state:");
            log.info(" - {} cars", carRepository.count());
            log.info(" - {} users", userRepository.count());
            log.info(" - {} bookings", bookingRepository.count());
        };
    }

    private User createOrUpdateUser(UserRepository userRepository, User user) {
        Optional<User> existingUser = userRepository.findByEmail(user.getEmail());
        if (existingUser.isPresent()) {
            User updatedUser = existingUser.get();
            updatedUser.setName(user.getName());
            updatedUser.setAvatarUrl(user.getAvatarUrl());
            updatedUser.setProvider(user.getProvider());
            updatedUser.setRole(user.getRole());
            return userRepository.save(updatedUser);
        } else {
            return userRepository.save(user);
        }
    }
}