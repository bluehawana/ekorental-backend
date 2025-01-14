package com.bluehawana.rentingcarsys.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class BookingRequest {
    private String carLicensePlate;
    private String userEmail;
    private String startTime;
    private String endTime;
    private Object carId;

    public String getCarLicensePlate() {
        return carLicensePlate;
    }

    public void setCarLicensePlate(String carLicensePlate) {
        this.carLicensePlate = carLicensePlate;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
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

    public Long getCarId() {
        return (Long) carId;
    }

    public Long getUserId() {
        return 1L;
    }

    public LocalDateTime getStartDate() {
        return LocalDateTime.parse(startTime);
    }

    public LocalDateTime getEndDate() {
        return LocalDateTime.parse(endTime);
    }

    public BigDecimal getTotalPrice() {
        return BigDecimal.valueOf(100);
    }
}
