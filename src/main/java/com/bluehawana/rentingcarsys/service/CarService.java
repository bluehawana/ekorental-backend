package com.bluehawana.rentingcarsys.service;

import com.bluehawana.rentingcarsys.model.Car;
import java.util.List;

public interface CarService {
    Car createCar(Car car);
    Car getCarById(Long id);
    Car getCarByLicensePlate(String licensePlate);
    List<Car> getAllCars();
    Car updateCar(Car car);
    void deleteCar(Long id);
}
