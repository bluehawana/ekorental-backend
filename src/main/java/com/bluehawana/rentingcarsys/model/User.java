package com.bluehawana.rentingcarsys.model;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    private String avatarUrl;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AuthProvider provider;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role = UserRole.USER;
    private String providerId;

    public User(Long userId) {
        this.id = userId;
    }

    public void setUsername(String username) {
        this.name = username;
    }

    public GoogleIdToken.Payload getPayload() {
        this.name = "test";
        return null; // Replace with actual return value
    }

    public void setProviderId(Object providerId) {
        this.providerId=providerId.toString();
    }
}

