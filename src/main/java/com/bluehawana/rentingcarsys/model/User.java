package com.bluehawana.rentingcarsys.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Booking> bookings = new ArrayList<>();

    @Setter
    @Column(unique = true, nullable = false)
    private String username;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    public Role getRole() {
        if (email.equals("bluehawana@gmail.com"))
            return Role.ADMIN;
        else
            return Role.USER;
    }

    public void addBooking(Booking booking) {
        bookings.add(booking);
        booking.setUser(this);
    }

    public void setRole(Role role) {
        if (role == Role.ADMIN) {
            email = "bluehawana@gmail.com";
    }
        else { role = Role.USER;
        }
    }

    public void setName(String name) {
        this.username = name;
    }
}

