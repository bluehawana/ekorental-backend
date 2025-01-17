package com.bluehawana.rentingcarsys.dto;

import com.bluehawana.rentingcarsys.model.Booking;
import com.bluehawana.rentingcarsys.model.BookingStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class BookingResponseDTO {
    private Long bookingId;
    private Long carId;
    private Long userId;
    private String startTime;
    private String endTime;
    private String status;
    private String totalPrice;

    public BookingResponseDTO(Long id, Long userId, LocalDateTime startTime, LocalDateTime endTime, LocalDateTime createdAt, BookingStatus status, BigDecimal totalPrice, Long carId) {
        this.bookingId = id;
        this.userId = userId;
        this.startTime = startTime.toString();
        this.endTime = endTime.toString();
        this.status = status.toString();
        this.totalPrice = totalPrice.toString();
        this.carId = carId;
    }

    public BookingResponseDTO(Booking booking) {
        this.bookingId = booking.getId();
        this.userId = booking.getUserId();
        this.startTime = booking.getStartTime().toString();
        this.endTime = booking.getEndTime().toString();
        this.status = booking.getStatus().toString();
        this.totalPrice = booking.getTotalPrice().toString();
        this.carId = booking.getCarId();
    }
}
