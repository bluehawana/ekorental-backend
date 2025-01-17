package com.bluehawana.rentingcarsys.service;

import com.bluehawana.rentingcarsys.model.Car;

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

    boolean isCarAvailable(Long carId, LocalDateTime startTime, LocalDateTime endTime);
}
