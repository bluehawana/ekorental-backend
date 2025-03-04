package com.bluehawana.rentingcarsys.dto;

import com.bluehawana.rentingcarsys.model.AuthProvider;
import com.bluehawana.rentingcarsys.model.UserRole;

public class UserDTO {
    private Long id;
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String address;
    private String role;

    public UserDTO() {
    }

    public UserDTO(Long id, String email, String password, String firstName, String lastName, String phoneNumber, String address, String role) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.role = role;
    }

    public UserDTO(Long id, String name, String email, String avatarUrl, String string, String string1) {
    }

    public UserDTO(Long userId) {
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public String getRole() {
        return role;
    }

    public String getName() {
        return firstName + " " + lastName;
    }

    public AuthProvider getProvider() {
        return AuthProvider.LOCAL;
    }

    public Object getProviderId() {
        return "providerId";
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setEmail(String carModel) {
        this.email = carModel;
    }

    public void setName(String carImage) {
        this.firstName = carImage;
    }
}
