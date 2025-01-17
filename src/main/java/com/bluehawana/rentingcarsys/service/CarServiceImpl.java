package com.bluehawana.rentingcarsys.service;

import com.bluehawana.rentingcarsys.model.Booking;
import com.bluehawana.rentingcarsys.model.Car;
import com.bluehawana.rentingcarsys.repository.CarRepository;
import com.bluehawana.rentingcarsys.repository.BookingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CarServiceImpl implements CarService {
    private final CarRepository carRepository;
    private final BookingRepository bookingRepository;

    @Override
    @Transactional
    public Car createCar(Car car) {
        return carRepository.save(car);
    }

    @Override
    public Car getCarById(Long id) {
        return carRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Car not found with id: " + id));
    }

    @Override
    public Optional<Car> getCarByLicensePlate(String licensePlate) {
        return carRepository.findByLicensePlate(licensePlate);
    }

    @Override
    public List<Car> getAllCars() {
        return carRepository.findAll();
    }

    @Override
    @Transactional
    public Car updateCar(Car car) {
        if (!carRepository.existsById(car.getId())) {
            throw new RuntimeException("Car not found with id: " + car.getId());
        }
        return carRepository.save(car);
    }

    @Override
    @Transactional
    public void deleteCar(Long id) {
        if (!carRepository.existsById(id)) {
            throw new RuntimeException("Car not found with id: " + id);
        }
        carRepository.deleteById(id);
    }

    @Override
    public boolean isCarAvailable(Long carId, LocalDateTime startTime, LocalDateTime endTime) {
        // Verify car exists
        if (!carRepository.existsById(carId)) {
            throw new RuntimeException("Car not found with id: " + carId);
        }

        // Check for overlapping bookings
        List<Booking> overlappingBookings = bookingRepository.findOverlappingBookings(
                carId, startTime, endTime);
        return overlappingBookings.isEmpty();
    }
}