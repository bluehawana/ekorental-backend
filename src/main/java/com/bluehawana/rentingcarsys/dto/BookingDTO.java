package com.bluehawana.rentingcarsys.dto;

import com.bluehawana.rentingcarsys.model.Booking;
import com.bluehawana.rentingcarsys.model.BookingStatus;
import com.bluehawana.rentingcarsys.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookingDTO {
    private Long id;
    private Long userId;
    private Long carId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String status;  // Changed from BookingStatus to String
    private BigDecimal totalPrice;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Convert from entity to DTO
    public static BookingDTO fromEntity(Booking booking) {
        return BookingDTO.builder()
                .id(booking.getId())
                .userId(booking.getUser().getId())
                .carId(booking.getCarId())
                .status(booking.getStatus().toString())
                .startTime(booking.getStartTime())
                .endTime(booking.getEndTime())
                .totalPrice(booking.getTotalPrice())
                .createdAt(booking.getCreatedAt())
                .updatedAt(booking.getUpdatedAt())
                .build();
    }

    // Convert from DTO to entity
    public Booking toEntity() {
        Booking booking = new Booking();
        booking.setId(this.id);
        User user = new User();
        user.setId(this.userId);
        booking.setUser(user);
        booking.setCarId(this.carId);
        booking.setStatus(String.valueOf(BookingStatus.valueOf(this.status)));
        booking.setStartTime(this.startTime);
        booking.setEndTime(this.endTime);
        booking.setTotalPrice(this.totalPrice);
        booking.setCreatedAt(this.createdAt);
        booking.setUpdatedAt(this.updatedAt);
        return booking;
    }
}