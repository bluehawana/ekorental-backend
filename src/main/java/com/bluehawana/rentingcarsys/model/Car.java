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

    @Column(nullable = false)
    private String model;

    @Column(nullable = false, unique = true)
    private String licensePlate;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal hourRate;

    private String imageUrl;

    @Column(nullable = false)
    private Boolean isAvailable = true;

    @Column(nullable = false)
    private String location;

    private Integer year;

    @Column(length = 500)
    private String description;

    public Car(Long carId) {
        this.id = carId;
    }

    public Optional<Car> map(Object o) {
        return Optional.empty();
    }

    public Object isAvailable() {
        if (isAvailable) {
            return true;
        }
        return false;
    }

    public void setAvailable(Object available) {
        if (available.equals(true)) {
            isAvailable = true;
        } else {
            isAvailable = false;
        }
    }
}