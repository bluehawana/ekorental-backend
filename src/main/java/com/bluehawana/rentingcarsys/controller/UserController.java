package com.bluehawana.rentingcarsys.controller;

import com.bluehawana.rentingcarsys.dto.UserDTO;
import com.bluehawana.rentingcarsys.model.User;
import com.bluehawana.rentingcarsys.model.UserRole;
import com.bluehawana.rentingcarsys.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://localhost:3000")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/role")
    public ResponseEntity<UserRole> getUserRole(@RequestParam String email) {
        UserRole role = userService.getUserRole(email);
        return ResponseEntity.ok(role);
    }

    @PostMapping
    public ResponseEntity<User> createOrUpdateUser(@RequestBody UserDTO userDTO) {
        User user = userService.createOrUpdateUser(userDTO);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        User user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserById(@PathVariable Long id) {
        userService.deleteUserById(id);
        return ResponseEntity.ok().build();
    }
}