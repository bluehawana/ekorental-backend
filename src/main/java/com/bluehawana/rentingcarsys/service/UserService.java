// In UserService.java
package com.bluehawana.rentingcarsys.service;

import com.bluehawana.rentingcarsys.dto.OAuthUserDTO;
import com.bluehawana.rentingcarsys.dto.UserDTO;
import com.bluehawana.rentingcarsys.model.AuthProvider;
import com.bluehawana.rentingcarsys.model.User;
import com.bluehawana.rentingcarsys.model.UserRole;
import com.bluehawana.rentingcarsys.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public abstract class UserService {

    private final UserRepository userRepository;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public UserRole getUserRole(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        return user.isPresent() ? user.get().getRole() : null;
    }

    public User createOrUpdateUser(UserDTO userDTO) {
        User user = userRepository.findById(userDTO.getId()).orElse(new User());
        user.setName(userDTO.getName());
        user.setEmail(userDTO.getEmail());
        user.setRole(userDTO.getRole());
        return userRepository.save(user);
    }

    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    public void deleteUserById(Long id) {
        userRepository.deleteById(id);
    }

    public User createOrUpdateOAuthUser(OAuthUserDTO oAuthUserDTO) {
        User user = userRepository.findByEmail(oAuthUserDTO.getEmail()).orElse(new User());
        user.setName(oAuthUserDTO.getName());
        user.setEmail(oAuthUserDTO.getEmail());
        user.setProvider(AuthProvider.valueOf(oAuthUserDTO.getProvider()));
        user.setAvatarUrl(oAuthUserDTO.getAvatarUrl());
        user.setRole(UserRole.valueOf(oAuthUserDTO.getRole().toUpperCase()));
        return userRepository.save(user);
    }

    public abstract User createOrUpdateUserFromGoogle(String email);

    public abstract User createOrUpdateUserFromGitHub(String email, String username);
}