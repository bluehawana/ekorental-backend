package com.bluehawana.rentingcarsys.service;

import com.bluehawana.rentingcarsys.dto.BookingDTO;
import com.bluehawana.rentingcarsys.dto.BookingResponseDTO;
import com.bluehawana.rentingcarsys.model.Booking;
import com.bluehawana.rentingcarsys.model.BookingStatus;
import com.bluehawana.rentingcarsys.model.Car;
import com.bluehawana.rentingcarsys.model.User;
import com.bluehawana.rentingcarsys.repository.BookingRepository;
import com.bluehawana.rentingcarsys.repository.CarRepository;
import com.bluehawana.rentingcarsys.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl extends BookingService {
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final CarRepository carRepository;

    @Override
    @Transactional
    public Booking createBooking(BookingDTO bookingDTO) {
        User user = userRepository.findById(bookingDTO.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Car car = carRepository.findById(bookingDTO.getCarId())
                .orElseThrow(() -> new RuntimeException("Car not found"));

        if (!isCarAvailable(car.getId(), bookingDTO.getStartTime(), bookingDTO.getEndTime())) {
            throw new RuntimeException("Car is not available for the selected time period");
        }

        Booking booking = Booking.builder()
                .userId(user.getId())
                .carId(car.getId())
                .status(BookingStatus.PENDING)
                .startTime(bookingDTO.getStartTime())
                .endTime(bookingDTO.getEndTime())
                .totalPrice(bookingDTO.getTotalPrice())
                .build();

        return bookingRepository.save(booking);
    }

    private boolean isCarAvailable(Long carId, LocalDateTime startTime, LocalDateTime endTime) {
        // Implementation to check car availability
        return true;
    }

    @Override
    public List<BookingResponseDTO> getAllBookings() {
        return bookingRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<Booking> getUserBookings(Long userId) {
        return List.of();
    }

    @Override
    public Booking getBookingById(Long id) {
        return null;
    }

    @Override
    public Booking updateBooking(Long id, BookingDTO bookingDTO) {
        return null;
    }

    @Override
    public void deleteBooking(Long id) {

    }

    private BookingResponseDTO convertToDTO(Booking booking) {
        return new BookingResponseDTO(
                booking.getId(),
                booking.getUserId(),
                booking.getStartTime(),
                booking.getEndTime(),
                booking.getCreatedAt(),
                booking.getStatus(),
                booking.getTotalPrice(),
                booking.getCarId()
        );
    }
}