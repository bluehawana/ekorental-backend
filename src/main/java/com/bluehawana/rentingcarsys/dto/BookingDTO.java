package com.bluehawana.rentingcarsys.dto;

import com.bluehawana.rentingcarsys.model.BookingStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class BookingDTO {
    private Long carId;
    private LocalDateTime startTime;  // Changed from startDate
    private LocalDateTime endTime;    // Changed from endDate
    private BigDecimal totalPrice;

    public LocalDateTime getStartDate() {
        return startTime;
    }

    public LocalDateTime getEndDate() {
        return endTime;
    }
}