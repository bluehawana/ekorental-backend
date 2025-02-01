package com.bluehawana.rentingcarsys.repository;

import com.bluehawana.rentingcarsys.model.Car;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CarRepository extends JpaRepository<Car, Long> {
    Optional<Car> findByLicensePlate(String licensePlate);

    List<Car> findByIsAvailable(boolean isAvailable);
    long count();  // This will count ALL cars
}