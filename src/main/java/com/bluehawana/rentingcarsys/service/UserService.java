package com.bluehawana.rentingcarsys.service;

import com.bluehawana.rentingcarsys.dto.OAuthUserDTO;
import com.bluehawana.rentingcarsys.dto.UserDTO;
import com.bluehawana.rentingcarsys.model.AuthProvider;
import com.bluehawana.rentingcarsys.model.ProviderType;
import com.bluehawana.rentingcarsys.model.User;
import com.bluehawana.rentingcarsys.model.UserRole;
import com.bluehawana.rentingcarsys.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public abstract class UserService {
    private final UserRepository userRepository;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> findByEmailAndProvider(String email, AuthProvider provider) {
        String providerId = "";
        return userRepository.findByEmailAndProvider(email, providerId);
    }

    @Transactional
    public User createOrUpdateOAuthUser(OAuthUserDTO oAuthUserDTO) {
        User user = userRepository.findByEmailAndProvider(
                oAuthUserDTO.getEmail(),
                String.valueOf(ProviderType.valueOf(oAuthUserDTO.getProvider().toUpperCase()))
        ).orElse(new User());

        user.setEmail(oAuthUserDTO.getEmail());
        user.setName(oAuthUserDTO.getName());
        user.setProviderId(oAuthUserDTO.getProviderId());
        user.setProvider(String.valueOf(AuthProvider.valueOf(String.valueOf(AuthProvider.valueOf(oAuthUserDTO.getProvider().toUpperCase())))));
        user.setAvatarUrl(oAuthUserDTO.getAvatarUrl());

        // Set default role if new user
        if (user.getRole() == null) {
            user.setRole(String.valueOf(UserRole.USER));
        }

        return userRepository.save(user);
    }

    @Transactional
    public User createOrUpdateUserFromGoogle(String email, String providerId, String name) {
        User user = userRepository.findByEmailAndProvider(email, String.valueOf(ProviderType.GOOGLE))
                .orElse(new User());

        user.setEmail(email);
        user.setProviderId(providerId);
        user.setProvider("GOOGLE");
        user.setName(name);
        user.setRole(String.valueOf(UserRole.USER));

        return userRepository.save(user);
    }

    @Transactional
    public User createOrUpdateUserFromGitHub(String email, String providerId, String username) {
        User user = userRepository.findByEmailAndProvider(email, String.valueOf(ProviderType.GITHUB))
                .orElse(new User());

        user.setEmail(email);
        user.setProviderId(providerId);
        user.setProvider(String.valueOf(AuthProvider.valueOf(String.valueOf(AuthProvider.GITHUB))));
        user.setName(username);
        user.setRole(String.valueOf(UserRole.USER));

        return userRepository.save(user);
    }

    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    public String getUserRole(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        return user.map(User::getRole).orElse(null);
    }

    public User createOrUpdateUser(UserDTO userDTO) {
        User user = userRepository.findById(userDTO.getId()).orElse(new User());

        user.setEmail(userDTO.getEmail());
        user.setName(userDTO.getName());
        user.setRole(userDTO.getRole());
        user.setProvider(String.valueOf(AuthProvider.valueOf(String.valueOf(AuthProvider.LOCAL))));

        return userRepository.save(user);
    }

    public void deleteUserById(Long id) {
        userRepository.deleteById(id);
    }

    public abstract User createOrUpdateUserFromGoogle(String email);

    public abstract User createOrUpdateUserFromGitHub(String email, String username);

    public User createOrUpdateGoogleUser(OAuthUserDTO userInfo) {
        return userRepository.findByEmail(userInfo.getEmail())
                .map(existingUser -> {
                    // Update existing user
                    existingUser.setName(userInfo.getName());
                    existingUser.setPicture(userInfo.getPicture());
                    existingUser.setProviderId(userInfo.getProviderId());
                    return userRepository.save(existingUser);
                })
                .orElseGet(() -> {
                    // Create new user
                    User newUser = new User();
                    newUser.setEmail(userInfo.getEmail());
                    newUser.setName(userInfo.getName());
                    newUser.setPicture(userInfo.getPicture());
                    newUser.setProviderId(userInfo.getProviderId());
                    newUser.setProvider("google");
                    newUser.setRole("USER");
                    return userRepository.save(newUser);
                });
    }

    public User processGoogleAuth(GoogleAuthRequest request) {
        return this.createOrUpdateGoogleUser(new OAuthUserDTO(
                request.getEmail(),
                request.getName(),
                request.getPicture(),
                request.getProviderId()
        ));
    }

    public User processGoogleAuth(String email, String providerId) {
    return this.createOrUpdateUserFromGoogle(email, providerId, email);
    }
}

class GoogleAuthRequest {
    private String email;
    private String name;
    private String picture;
    private String providerId;

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getPicture() {
        return picture;
    }

    public String getProviderId() {
        return providerId;
    }

    // Other fields and methods
}