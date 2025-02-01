package com.bluehawana.rentingcarsys.service;

import com.bluehawana.rentingcarsys.model.Car;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface CarService {
    Car createCar(Car car);
    Car getCarById(Long id);
    Optional<Car> getCarByLicensePlate(String licensePlate);
    List<Car> getAllCars();
    Car updateCar(Car car);
    void deleteCar(Long id);

    List<Car> getAvailableCars();

    Car updateCarAvailability(Long id, boolean available);
    boolean checkCarAvailability(Long id, String startDate, String endDate);
    List<Car> getAvailableCars(String startDate, String endDate);
    void handleBookingStatusChange(Long carId, String bookingStatus);

    boolean isCarAvailable(Long carId, LocalDateTime startTime, LocalDateTime endTime);

    BigDecimal calculateTotalPrice(Car car, LocalDateTime startDate, LocalDateTime endDate);
}