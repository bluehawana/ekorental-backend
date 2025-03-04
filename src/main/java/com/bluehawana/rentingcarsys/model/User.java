package com.bluehawana.rentingcarsys.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;

    private String name;
    private String picture;
    private String providerId;  // This will store Google's sub/ID

    @Column(nullable = false)
    private String provider = "google";  // Default to google

    private String role = "USER";

    public void setAvatarUrl(String avatarUrl) {
        this.picture = avatarUrl;
    }

    public void setUsername(String username) {
        this.name = username;
    }
}
