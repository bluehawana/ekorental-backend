package com.bluehawana.rentingcarsys.service;

import com.bluehawana.rentingcarsys.model.UserRole;
import com.bluehawana.rentingcarsys.model.User;
import com.bluehawana.rentingcarsys.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl extends UserService {
    @Autowired
    private UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        super(userRepository);
    }

    @Override
    public String getUserRole(String email) {
        return userRepository.findByEmail(email)
                .map(User::getRole)
                .orElse(String.valueOf(UserRole.USER));
    }

    @Override
    public User createOrUpdateUserFromGoogle(String email) {
        return createOrUpdateUser(email, email);
    }

    @Override
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
