package com.bluehawana.rentingcarsys.service;

import com.bluehawana.rentingcarsys.model.Role;
import com.bluehawana.rentingcarsys.model.User;
import com.bluehawana.rentingcarsys.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public Role getUserRole(String email) {
        return userRepository.findByEmail(email)
                .map(User::getRole)
                .orElse(Role.USER);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public void handleLogin(String email, String username) {
        userRepository.findByEmail(email).ifPresentOrElse(
                user -> {
                    // User exists, do nothing
                },
                () -> {
                    // User does not exist, create new user
                    User newUser = new User();
                    newUser.setEmail(email);
                    newUser.setUsername(username);
                    newUser.setRole(Role.USER);
                    userRepository.save(newUser);
                }
        );
    }

    public User createOrUpdateUserFromGoogle(String email) {
        return createOrUpdateUser(email, email);
    }

    public User createOrUpdateUserFromGitHub(String email, String username) {
        return createOrUpdateUser(email, username);
    }

    private User createOrUpdateUser(String email, String username) {
        Optional<User> existingUser = userRepository.findByEmail(email);

        User user;
        if (existingUser.isPresent()) {
            user = existingUser.get();
        } else {
            user = new User();
        }

        user.setUsername(username);
        user.setEmail(email);

        return userRepository.save(user);
    }
}



