package com.bluehawana.rentingcarsys.dto;

import com.bluehawana.rentingcarsys.model.BookingStatus;
import lombok.Data;
import lombok.Getter;

import java.time.LocalDateTime;
import java.math.BigDecimal;

@Data
public class BookingRequestDTO {
    private Long carId;
    private Long userId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private BigDecimal totalPrice;
    @Getter
    private Object userEyanyanmail;

    public long getTotalHours() {
        return java.time.Duration.between(startTime, endTime).toHours();
    }

    public String getUserName() {
        return "User";
    }

    public BookingStatus getStatus() {
        return BookingStatus.PENDING;

    }
}



