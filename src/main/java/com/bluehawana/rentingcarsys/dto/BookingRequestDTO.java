package com.bluehawana.rentingcarsys.dto;

import com.bluehawana.rentingcarsys.model.BookingStatus;
import lombok.Data;

import java.time.LocalDateTime;
import java.math.BigDecimal;

@Data
public class BookingRequestDTO {
    private Long carId;
    private Long userId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private BigDecimal totalPrice;

    public long getTotalHours() {
        return java.time.Duration.between(startTime, endTime).toHours();
    }

    public BookingStatus getStatus() {
        return BookingStatus.PENDING;
    }
}