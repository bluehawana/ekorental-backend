// src/main/java/com/bluehawana/rentingcarsys/controller/UserController.java

package com.bluehawana.rentingcarsys.controller;

import com.bluehawana.rentingcarsys.dto.OAuthUserDTO;
import com.bluehawana.rentingcarsys.dto.UserDTO;
import com.bluehawana.rentingcarsys.model.User;
import com.bluehawana.rentingcarsys.service.UserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final Logger logger = LoggerFactory.getLogger(UserController.class);

    @PostMapping("/oauth")
    public ResponseEntity<?> handleOAuthUser(@RequestBody OAuthUserDTO request) {
        try {
            logger.info("Received OAuth request: {}", request);
            User user = userService.createOrUpdateOAuthUser(request);
            logger.info("Created/Updated user: {}", user);
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            logger.error("Error handling OAuth user", e);
            return ResponseEntity.badRequest()
                    .body(Map.of("message", e.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        User user = userService.getUserById(id);
        if (user != null) {
            return ResponseEntity.ok(user);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody UserDTO userDTO) {
        return ResponseEntity.ok(userService.createOrUpdateUser(userDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody UserDTO userDTO) {
        userDTO.setId(id);
        return ResponseEntity.ok(userService.createOrUpdateUser(userDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        userService.deleteUserById(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/role")
    public ResponseEntity<?> getUserRole(@RequestParam String email) {
        return ResponseEntity.ok(userService.getUserRole(email));
    }
}