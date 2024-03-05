package org.example.jobapp.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.jobapp.dto.CommonResponse;
import org.example.jobapp.dto.User;
import org.example.jobapp.keycloakclient.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.ZonedDateTime;

@RestController
@RequestMapping("/api/v1/auth")
@SecurityRequirement(name = "Keycloak")
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody @Valid User user) {
        userService.registerUser(user);
        CommonResponse<?> response = CommonResponse.builder()
                .success(true)
                .timestamp(ZonedDateTime.now())
                .message("Successfully create user")
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }




}
