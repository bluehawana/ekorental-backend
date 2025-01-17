package com.bluehawana.rentingcarsys.dto;

import com.bluehawana.rentingcarsys.model.BookingStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.springframework.http.HttpStatus.ACCEPTED;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingDTO {
    private Long userId;
    private Long carId;
    private LocalDateTime startTime;  // Must match frontend field name
    private LocalDateTime endTime;    // Must match frontend field name
    private BigDecimal totalPrice;

    public BookingStatus getStatus() {
        return BookingStatus.PENDING;
    }
}