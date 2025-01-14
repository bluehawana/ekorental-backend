package com.bluehawana.rentingcarsys.dto;

import com.bluehawana.rentingcarsys.model.User;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class BookingDTO {
    private Long id;
    private String carLicensePlate;
    private String userEmail;
    private static LocalDateTime startDate;
    private static LocalDateTime endDate;
    private String status;

    public BookingDTO() {
    }

    public BookingDTO(Long id, String carLicensePlate, String userEmail, LocalDateTime startDate, LocalDateTime endDate, String status) {
        this.id = id;
        this.carLicensePlate = carLicensePlate;
        this.userEmail = userEmail;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
    }

    public static BookingDTO getCar() {
        return new BookingDTO();
    }

    public static GoogleIdToken.Payload getUser() {
        User user = new User();
        return user.getPayload();
    }

    public static BigDecimal getTotalPrice() {
        return BigDecimal.ZERO;
    }

    public Long getId() {
        return id;
    }

    public String getCarLicensePlate() {
        return carLicensePlate;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public static LocalDateTime getStartDate() {
        return startDate;
    }

    public static LocalDateTime getEndDate() {
        return endDate;
    }

    public String getStatus() {
        return status;
    }
}