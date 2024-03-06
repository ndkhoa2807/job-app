package org.example.jobapp.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.jobapp.dto.CommonResponse;
import org.example.jobapp.dto.User;
import org.example.jobapp.keycloakclient.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.ZonedDateTime;


@RestController
@RequestMapping("/api/v1/auth")
@SecurityRequirement(name = "Keycloak")
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;

    @Value("${security.logout-endpoint}")
    private String LOGOUT_URL;
    @Value("${security.logout-redirect}")
    private String REDIRECT_URI;

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

    @GetMapping("/logout")
    public String logout(HttpServletRequest request) throws ServletException {
        request.logout();
        StringBuilder stringBuilder = new StringBuilder();
        return stringBuilder.append("redirect:").append(LOGOUT_URL).append("?redirect_uri=").append(REDIRECT_URI).toString();
    }




}
