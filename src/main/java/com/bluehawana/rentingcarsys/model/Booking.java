package com.bluehawana.rentingcarsys.model;

import com.bluehawana.rentingcarsys.dto.UserDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "bookings")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", insertable = false, updatable = false)
    private Long userId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "car_id", insertable = false, updatable = false)
    private Long carId;

    @ManyToOne
    @JoinColumn(name = "car_id", nullable = false) // Foreign key to the Car table
    private Car car;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BookingStatus status;

    @Column(nullable = false)
    private LocalDateTime startTime;

    @Column(nullable = false)
    private LocalDateTime endTime;

    @Column(nullable = false)
    private BigDecimal totalPrice;

    @Column(updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    public void setUser(User user) {
        this.user = user;
        this.userId = user.getId(); // Synchronize the ID field
    }

    public void setCar(Car car) {
        this.car = car;
        this.carId = car.getId(); // Synchronize the ID field
    }

    public UserDTO getUserDTO() {
        return new UserDTO(userId);
    }
}