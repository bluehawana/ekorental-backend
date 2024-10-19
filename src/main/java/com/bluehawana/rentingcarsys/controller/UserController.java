package com.bluehawana.rentingcarsys.controller;

import com.bluehawana.rentingcarsys.model.Role;
import com.bluehawana.rentingcarsys.model.User;
import com.bluehawana.rentingcarsys.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/role")
    public ResponseEntity<Role> getUserRole(@RequestParam String email) {
        Role role = userService.getUserRole(email);
        return ResponseEntity.ok(role);
    }
}
