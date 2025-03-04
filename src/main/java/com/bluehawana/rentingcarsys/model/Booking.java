package com.bluehawana.rentingcarsys.model;

import com.bluehawana.rentingcarsys.dto.UserDTO;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "bookings")
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private Long carId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String status;
    private BigDecimal totalPrice;
    private Integer totalHours;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Transient
    private String carModel;

    @Transient
    private String carImage;

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public UserDTO getCar() {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(this.carId);
        userDTO.setEmail(this.carModel);
        userDTO.setName(this.carImage);
        return userDTO;
    }

    public void setCar(Car firstCar) {
        this.carId = firstCar.getId();
        this.carModel = firstCar.getModel();
        this.carImage = firstCar.getImageUrl();
    }
}