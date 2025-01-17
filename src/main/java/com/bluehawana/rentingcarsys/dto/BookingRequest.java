package com.bluehawana.rentingcarsys.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class BookingRequest {
    private Long userId;
    private Long carId;
    private String startTime;
    private String endTime;
    private BigDecimal totalPrice;

    public BookingRequest(Long carId, Long userId) {
        this.carId = carId;
        this.userId = userId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getCarId() {
        return carId;
    }

    public void setCarId(Long carId) {
        this.carId = carId;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public LocalDateTime getStartDate() {
        return LocalDateTime.parse(startTime);
    }

    public LocalDateTime getEndDate() {
        return LocalDateTime.parse(endTime);
    }
}