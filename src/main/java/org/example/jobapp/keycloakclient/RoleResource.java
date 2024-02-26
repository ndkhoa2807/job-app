package org.example.jobapp.keycloakclient;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.example.jobapp.dto.CommonResponse;
import org.example.jobapp.dto.Role;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.ZonedDateTime;
import java.util.List;

@RestController
@RequestMapping("/keycloak")
@SecurityRequirement(name = "Keycloak")
@RequiredArgsConstructor
public class RoleResource {
    private final RoleService roleService;

    @GetMapping(value = "/roles")
    public ResponseEntity<?> getRoles() {
        List<Role> roles = roleService.getRoles();
        CommonResponse<?> response = CommonResponse.builder()
                .success(true)
                .timestamp(ZonedDateTime.now())
                .data(roles)
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/roles/{roleName}")
    public ResponseEntity<?> getRole(@PathVariable("roleName") String roleName) {
        Role role = roleService.getRole(roleName);
        CommonResponse<?> response = CommonResponse.builder()
                .success(true)
                .timestamp(ZonedDateTime.now())
                .data(role)
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/role")
    public ResponseEntity<?> createRole(@RequestBody Role role) {
        role = roleService.createRole(role);
        CommonResponse<?> response = CommonResponse.builder()
                .success(true)
                .timestamp(ZonedDateTime.now())
                .message("Successfully created role")
                .data(role)
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/role/{roleName}")
    public ResponseEntity<?> deleteRole(@PathVariable("roleName") String roleName) {
        roleService.deleteRole(roleName);
        CommonResponse<?> response = CommonResponse.builder()
                .success(true)
                .timestamp(ZonedDateTime.now())
                .message("Successfully deleted role")
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
