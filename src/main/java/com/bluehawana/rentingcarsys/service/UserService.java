package com.bluehawana.rentingcarsys.service;

import com.bluehawana.rentingcarsys.model.Role;
import com.bluehawana.rentingcarsys.model.User;
import com.bluehawana.rentingcarsys.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

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
}



