package com.bluehawana.rentingcarsys.dto;

import com.bluehawana.rentingcarsys.model.BookingStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class BookingDTO {
    private Long carId;
    private String userEmail;
    private String providerId;
    private String providerType;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
}