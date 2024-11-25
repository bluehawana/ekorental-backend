package com.bluehawana.rentingcarsys.model;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
public class Car {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String make;
    private String model;
    private int year;

    @Column(name = "price_per_hour") // Adjust column name for better readability
    private double pricePerHour; // Change variable name to camelCase for consistency

    @Column(name = "license_plate") // Adjust column name for better readability
    private String licensePlate; // Change variable name to camelCase for consistency

    private String color;

    private String imageUrl; // Add imageUrl field

    // Default constructor
    public Car() {
    }

    // Constructor with parameters
    public Car(String make, String model, int year, double pricePerHour, String licensePlate, String color) {
        this.make = make;
        this.model = model;
        this.year = year;
        this.pricePerHour = pricePerHour;
        this.licensePlate = licensePlate;
        this.color = color;
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public BigDecimal getPricePerHour() {
        return BigDecimal.valueOf(pricePerHour);
    }

    public void setPricePerHour(double pricePerHour) {
        this.pricePerHour = pricePerHour;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

        private LocalDateTime createdAt;

public void setCreatedAt(LocalDateTime createdAt) {
    this.createdAt = createdAt;
}

public LocalDateTime getCreatedAt() {
    return createdAt;
}

    public void setUpdatedAt(LocalDateTime now) {
        this.createdAt = now;
    }
}

