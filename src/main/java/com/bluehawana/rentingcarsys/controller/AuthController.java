package com.bluehawana.rentingcarsys.controller;

import com.bluehawana.rentingcarsys.model.User;
import com.bluehawana.rentingcarsys.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {
    private final UserService userService;

    @PostMapping("/google")
    public ResponseEntity<?> handleGoogleAuth(@RequestBody GoogleAuthRequest request) {
        log.info("Received Google auth request for email: {}", request.getEmail());
        try {
            if (request.getEmail() == null || request.getProviderId() == null) {
                log.error("Missing required fields in request");
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Email and providerId are required"));
            }

            User user = userService.processGoogleAuth(request.getEmail(), request.getProviderId());
            log.info("Successfully processed Google auth for user: {}", user.getEmail());
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            log.error("Error processing Google auth", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                            "error", "Failed to process authentication",
                            "message", e.getMessage()
                    ));
        }
    }
}

class GoogleAuthRequest {
    private String email;
    private String providerId;

    public String getEmail() {
        return email;
    }

    public String getProviderId() {
        return providerId;
    }

    // Other fields and methods
}