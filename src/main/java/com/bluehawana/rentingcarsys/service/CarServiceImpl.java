package com.bluehawana.rentingcarsys.service;

import com.bluehawana.rentingcarsys.exception.ResourceNotFoundException;
import com.bluehawana.rentingcarsys.model.Car;
import com.bluehawana.rentingcarsys.repository.CarRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@Service
@Transactional
@RequiredArgsConstructor
public class CarServiceImpl implements CarService {
    private final CarRepository carRepository;
    private static final Logger logger = LoggerFactory.getLogger(CarServiceImpl.class);

    @Override
    public Car createCar(Car car) {
        return null;
    }

    @Override
    public Car getCarById(Long id) {
        logger.info("Attempting to fetch car with ID: {}", id);
        Car car = carRepository.findById(id)
                .orElse(null);
        if (car != null) {
            logger.info("Found car: {}", car);
        } else {
            logger.warn("No car found with ID: {}", id);
        }
        return car;
    }

    @Override
    public Optional<Car> getCarByLicensePlate(String licensePlate) {
        return Optional.empty();
    }

    @Override
    public List<Car> getAllCars() {
        return carRepository.findAll();  // Return ALL cars regardless of availability
    }

    @Override
    public Car updateCar(Car car) {
        return null;
    }

    @Override
    public void deleteCar(Long id) {

    }

    @Override
    public List<Car> getAvailableCars() {
        return carRepository.findByIsAvailable(true);  // Only for filtering available cars
    }

    @Override
    public Car updateCarAvailability(Long carId, boolean available) {
        Car car = carRepository.findById(carId)
                .orElseThrow(() -> new ResourceNotFoundException("Car not found"));
        car.setAvailable(available);
        carRepository.save(car);
        return car;
    }

    @Override
    public boolean checkCarAvailability(Long id, String startDate, String endDate) {
        return false;
    }

    @Override
    public List<Car> getAvailableCars(String startDate, String endDate) {
        return List.of();
    }

    @Override
    public void handleBookingStatusChange(Long carId, String bookingStatus) {

    }

    @Override
    public boolean isCarAvailable(Long carId, LocalDateTime startTime, LocalDateTime endTime) {
        return false;
    }

    @Override
    public BigDecimal calculateTotalPrice(Car car, LocalDateTime startDate, LocalDateTime endDate) {
        return null;
    }
}