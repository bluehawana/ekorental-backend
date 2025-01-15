package com.bluehawana.rentingcarsys.config;

import com.bluehawana.rentingcarsys.model.*;
import com.bluehawana.rentingcarsys.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Optional;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initDatabase(
            CarRepository carRepository,
            UserRepository userRepository) {
        return args -> {
            // First, delete all existing cars to prevent duplicates
            carRepository.deleteAll();

            // Create 6 Tesla cars with simple hourly rates
            Arrays.asList(
                    createOrUpdateCar(carRepository, Car.builder()
                            .model("Tesla Model 3")
                            .licensePlate("ABC123")
                            .hourRate(new BigDecimal("110.00"))
                            .imageUrl("http://localhost:8080/uploads/model_3.png")
                            .isAvailable(true)
                            .location("Chalmers University")
                            .year(2024)
                            .description("Model 3: Most economical choice. Perfect for daily commute.")
                            .build()),

                    createOrUpdateCar(carRepository, Car.builder()
                            .model("Tesla Model Y")
                            .licensePlate("DEF456")
                            .hourRate(new BigDecimal("120.00"))
                            .imageUrl("http://localhost:8080/uploads/model_y.png")
                            .isAvailable(true)
                            .location("Lindholmen Science Park")
                            .year(2024)
                            .description("Model Y: Practical family choice with great efficiency.")
                            .build()),

                    createOrUpdateCar(carRepository, Car.builder()
                            .model("Tesla Model S")
                            .licensePlate("GHI789")
                            .hourRate(new BigDecimal("150.00"))
                            .imageUrl("http://localhost:8080/uploads/model_s.png")
                            .isAvailable(true)
                            .location("Lindholmen Science Park")
                            .year(2024)
                            .description("Model S: Luxury sedan with impressive range.")
                            .build()),

                    createOrUpdateCar(carRepository, Car.builder()
                            .model("Tesla Model X")
                            .licensePlate("JKL012")
                            .hourRate(new BigDecimal("170.00"))
                            .imageUrl("http://localhost:8080/uploads/model_x.png")
                            .isAvailable(true)
                            .location("Nordstan Park")
                            .year(2024)
                            .description("Model X: Spacious SUV perfect for group travel.")
                            .build()),

                    createOrUpdateCar(carRepository, Car.builder()
                            .model("Tesla Cybertruck")
                            .licensePlate("MNO345")
                            .hourRate(new BigDecimal("250.00"))
                            .imageUrl("http://localhost:8080/uploads/cybertruck.png")
                            .isAvailable(true)
                            .location("Chalmers University")
                            .year(2024)
                            .description("Cybertruck: Future of electric utility vehicles.")
                            .build()),

                    createOrUpdateCar(carRepository, Car.builder()
                            .model("Tesla Roadster")
                            .licensePlate("PQR678")
                            .hourRate(new BigDecimal("300.00"))
                            .imageUrl("http://localhost:8080/uploads/roadster.png")
                            .isAvailable(true)
                            .location("Nordstan Park")
                            .year(2024)
                            .description("Roadster: High-performance sports car.")
                            .build())
            );

            // Create or update admin user
            createOrUpdateUser(userRepository, User.builder()
                    .name("Admin User")
                    .email("admin@ekorental.com")
                    .avatarUrl("https://api.dicebear.com/7.x/avataaars/svg?seed=Admin")
                    .provider(AuthProvider.EMAIL)
                    .role(UserRole.ADMIN)
                    .build());
        };
    }

    private Car createOrUpdateCar(CarRepository repository, Car newCar) {
        Optional<Car> existingCarOptional = repository.findByLicensePlate(newCar.getLicensePlate());
        if (existingCarOptional.isPresent()) {
            Car existingCar = existingCarOptional.get();
            existingCar.setModel(newCar.getModel());
            existingCar.setHourRate(newCar.getHourRate());
            existingCar.setImageUrl(newCar.getImageUrl());
            existingCar.setIsAvailable(newCar.getIsAvailable());
            existingCar.setLocation(newCar.getLocation());
            existingCar.setYear(newCar.getYear());
            existingCar.setDescription(newCar.getDescription());
            return repository.save(existingCar);
        }
        return repository.save(newCar);
    }

    private User createOrUpdateUser(UserRepository repository, User newUser) {
        Optional<User> existingUserOptional = repository.findByEmail(newUser.getEmail());
        if (existingUserOptional.isPresent()) {
            User existingUser = existingUserOptional.get();
            existingUser.setName(newUser.getName());
            existingUser.setAvatarUrl(newUser.getAvatarUrl());
            existingUser.setProvider(newUser.getProvider());
            existingUser.setRole(newUser.getRole());
            return repository.save(existingUser);
        }
        return repository.save(newUser);
    }
}