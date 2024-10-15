package com.bluehawana.rentingcarsys.service;

import com.bluehawana.rentingcarsys.model.Car;
import com.bluehawana.rentingcarsys.repository.CarRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CarServiceImpl implements CarService {
    private static final Logger logger = LoggerFactory.getLogger(CarServiceImpl.class);

    @Autowired
    private CarRepository carRepository;

    @Override
    public Car createCar(Car car) {
        logger.info("Creating new car: {}", car);
        if (car == null) {
            logger.error("Attempted to create null car");
            throw new IllegalArgumentException("Car cannot be null");
        }
        return carRepository.save(car);
    }

    @Override
    public Car getCarById(Long id) {
        logger.info("Fetching car with id: {}", id);
        Optional<Car> carOptional = carRepository.findById(id);
        if (carOptional.isPresent()) {
            logger.info("Found car: {}", carOptional.get());
            return carOptional.get();
        } else {
            logger.warn("Car with id {} not found", id);
            return null;
        }
    }

    @Override
    public List<Car> getAllCars() {
        logger.info("Fetching all cars");
        List<Car> cars = carRepository.findAll();
        logger.info("Found {} cars", cars.size());
        return cars;
    }

    @Override
    public Car updateCar(Car car) {
        logger.info("Updating car: {}", car);
        if (car == null || car.getId() == null) {
            logger.error("Attempted to update null car or car without id");
            throw new IllegalArgumentException("Car cannot be null and must have an id");
        }
        return carRepository.save(car);
    }

    @Override
    public void deleteCar(Long id) {
        logger.info("Deleting car with id: {}", id);
        carRepository.deleteById(id);
    }

    @Override
    public Car getCarByLicensePlate(String licensePlate) {
        logger.info("Fetching car with license plate: {}", licensePlate);
        Car car = carRepository.findByLicensePlate(licensePlate);
        if (car != null) {
            logger.info("Found car: {}", car);
        } else {
            logger.warn("Car with license plate {} not found", licensePlate);
        }
        return car;
    }
}