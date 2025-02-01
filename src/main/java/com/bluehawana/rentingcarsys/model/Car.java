package com.bluehawana.rentingcarsys.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.util.Optional;

@Entity
@Table(name = "cars")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Car {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String model;
    private String licensePlate;
    private BigDecimal hourRate;
    private String imageUrl;
    private boolean isAvailable;
    private String location;
    private Integer year;
    private String description;

    public BigDecimal getPricePerDay() {
        return hourRate.multiply(BigDecimal.valueOf(24));
    }

    // Add any other fields you have
}