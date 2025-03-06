package com.example.accounting_service.controllers;

import com.example.accounting_service.dto.JwtAuthenticationResponse;
import com.example.accounting_service.dto.UserDto;
import com.example.accounting_service.services.AuthService;
import com.example.accounting_service.services.GoogleOAuthService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
@CrossOrigin(origins = "*", maxAge = 3600)
public class AuthController {

    private final Logger logger = LoggerFactory.getLogger(AuthController.class);

    private final AuthService authService;
    private final GoogleOAuthService googleOAuthService;

    @PostMapping("/login")
    public ResponseEntity<JwtAuthenticationResponse> login(@Valid @RequestBody UserDto loginRequest) {
        this.logger.info("login request received");

        return ResponseEntity.ok(authService.authenticateUser(loginRequest));
    }

    @PostMapping("/register")
    public ResponseEntity<Void> register(@Valid @RequestBody UserDto registerRequest) {

        this.logger.info("Register request received");
        authService.registerUser(registerRequest);
        return new ResponseEntity<>(HttpStatus.CREATED);

    }

    @GetMapping("/google/auth-url")
    public ResponseEntity<Map<String, String>> getGoogleAuthUrl() {
        try {
            String authUrl = googleOAuthService.generateAuthorizationUrl();
            Map<String, String> response = new HashMap<>();
            response.put("authUrl", authUrl);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/google/callback")
    public ResponseEntity<JwtAuthenticationResponse> handleGoogleCallback(@RequestParam String code, HttpServletResponse response) throws IOException {
        try {
            this.logger.info("Processing google oAuth callback request");
            JwtAuthenticationResponse authResponse = googleOAuthService.processCode(code);
            String token = authResponse.getAccessToken();

            response.sendRedirect("http://localhost:4200/oauth2/callback?token=" + token);
            return ResponseEntity.ok(authResponse);
        } catch (Exception e) {
            logger.error("Error processing Google OAuth callback", e);
            response.sendRedirect("http://localhost:4200/login?error=oauth_failed");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}

