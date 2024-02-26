package org.example.jobapp.keycloakclient;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.jobapp.dto.CommonResponse;
import org.example.jobapp.dto.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.ZonedDateTime;
import java.util.List;

@RestController
@RequestMapping("/keycloak")
@SecurityRequirement(name = "Keycloak")
@RequiredArgsConstructor
public class UserResource {

    private final UserService userService;

    @GetMapping("/user")
    public ResponseEntity<?> getUsers() {
        List<User> userList = userService.getUsers();
        CommonResponse<?> response = new CommonResponse<>(
                true,
                null,
                ZonedDateTime.now(),
                userList
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @PostMapping("/user")
    public ResponseEntity<?> createUser(@RequestBody @Valid User user) {
        userService.registerUser(user);
        CommonResponse<?> response = CommonResponse.builder()
                .success(true)
                .timestamp(ZonedDateTime.now())
                .message("Successfully create user")
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping(value = "/user")
    public ResponseEntity<?> updateUser(@RequestBody @Valid User user) {
        user = userService.updateUser(user);
        CommonResponse<?> response = CommonResponse.builder()
                .success(true)
                .timestamp(ZonedDateTime.now())
                .message("Successfully update user")
                .data(user)
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(value = "/users/{id}")
    public ResponseEntity<?> getUser(@PathVariable("id") String id) {
        User user = userService.getUser(id);
        CommonResponse<?> response = CommonResponse.builder()
                .success(true)
                .timestamp(ZonedDateTime.now())
                .data(user)
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable("id") String id) {
        userService.deleteUser(id);
        CommonResponse<?> response = CommonResponse.builder()
                .success(true)
                .message("Successfully delete user")
                .timestamp(ZonedDateTime.now())
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


}
