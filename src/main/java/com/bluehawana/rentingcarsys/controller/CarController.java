package com.bluehawana.rentingcarsys.controller;

import com.bluehawana.rentingcarsys.model.Car;
import com.bluehawana.rentingcarsys.service.CarService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Map;



@RestController
@RequestMapping("/api/cars")
@RequiredArgsConstructor
public class CarController {
    private final CarService carService;
    private static final Logger logger = LoggerFactory.getLogger(CarController.class);

    @PostMapping
    public ResponseEntity<?> createCar(@RequestBody Car car) {
        try {
            Car createdCar = carService.createCar(car);
            return ResponseEntity.ok(createdCar);
        } catch (Exception e) {
            logger.error("Error creating car", e);
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCar(@PathVariable Long id) {
        try {
            Car car = carService.getCarById(id);
            if (car == null) {
                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body(Map.of("message", "Car not found with id: " + id));
            }
            return ResponseEntity.ok(car);
        } catch (Exception e) {
            logger.error("Error fetching car with id: " + id, e);
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", e.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllCars() {
        try {
            logger.debug("getAllCars method called");
            List<Car> cars = carService.getAllCars();
            return ResponseEntity.ok(cars);
        } catch (Exception e) {
            logger.error("Error fetching all cars", e);
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Error fetching cars: " + e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateCar(@PathVariable Long id, @RequestBody Car car) {
        try {
            car.setId(id);
            Car updatedCar = carService.updateCar(car);
            return ResponseEntity.ok(updatedCar);
        } catch (Exception e) {
            logger.error("Error updating car with id: " + id, e);
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCar(@PathVariable Long id) {
        try {
            carService.deleteCar(id);
            return ResponseEntity.ok(Map.of("message", "Car deleted successfully"));
        } catch (Exception e) {
            logger.error("Error deleting car with id: " + id, e);
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", e.getMessage()));
        }
    }
}