package com.bluehawana.rentingcarsys.controller;

import com.bluehawana.rentingcarsys.model.Car;
import com.bluehawana.rentingcarsys.service.CarService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cars")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class CarController {
    @Autowired
    private CarService carService;

    private static final Logger logger = LoggerFactory.getLogger(CarController.class);

    // Get all cars - public access
    @GetMapping
    public ResponseEntity<List<Car>> getAllCars() {
        logger.debug("getAllCars method called");
        return ResponseEntity.ok(carService.getAllCars());
    }

    // Get single car - public access
    @GetMapping("/{id}")
    public ResponseEntity<Car> getCar(@PathVariable Long id) {
        return ResponseEntity.ok(carService.getCarById(id));
    }

    // Admin only endpoints
    @PostMapping
    public ResponseEntity<Car> createCar(@RequestBody Car car) {
        return ResponseEntity.ok(carService.createCar(car));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Car> updateCar(@PathVariable Long id, @RequestBody Car car) {
        car.setId(id);
        return ResponseEntity.ok(carService.updateCar(car));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCar(@PathVariable Long id) {
        carService.deleteCar(id);
        return ResponseEntity.ok().build();
    }
}