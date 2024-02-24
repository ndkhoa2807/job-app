package org.example.jobapp.keycloakclient;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.example.jobapp.config.KeycloakSecurityUtil;
import org.example.jobapp.dto.CommonResponse;
import org.example.jobapp.dto.User;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.common.util.CollectionUtil;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/keycloak")
@SecurityRequirement(name = "Keycloak")
@RequiredArgsConstructor
public class UserResource {
    private final KeycloakSecurityUtil keycloakSecurityUtil;

    @Value("${security.realm}")
    private String realm;

    @GetMapping("/user")
    public List<User> getUsers() {
        Keycloak keycloak = keycloakSecurityUtil.getKeycloakInstance();
        List<UserRepresentation> userRepresentations = keycloak
                .realm(realm)
                .users().list();
        return mapUsers(userRepresentations);
    }


    @PostMapping("/user")
    public ResponseEntity<?> createUser(@RequestBody @Valid User user) {
        UserRepresentation userRepresentation = mapUserRep(user);
        Keycloak keycloak = keycloakSecurityUtil.getKeycloakInstance();
        keycloak.realm(realm).users().create(userRepresentation);
        CommonResponse<?> response = CommonResponse.builder()
                .success(true)
                .data(user)
                .timestamp(ZonedDateTime.now())
                .message("Successfully create user")
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    private List<User> mapUsers(List<UserRepresentation> userRepresentations) {
        List<User> users = new ArrayList<>();
        if (CollectionUtil.isNotEmpty(userRepresentations)) {
            userRepresentations.forEach(userRepresentation -> {
                users.add(mapUser(userRepresentation));
            });
        }
        return users;
    }

    private User mapUser(UserRepresentation userRepresentation) {
        return User.builder()
                .id(userRepresentation.getId())
                .firstname(userRepresentation.getFirstName())
                .lastname(userRepresentation.getLastName())
                .email(userRepresentation.getEmail())
                .username(userRepresentation.getUsername())
                .build();
    }

    private UserRepresentation mapUserRep(User user) {
        UserRepresentation userRepresentation = new UserRepresentation();
        userRepresentation.setId(user.getId());
        userRepresentation.setUsername(user.getUsername());
        userRepresentation.setFirstName(user.getFirstname());
        userRepresentation.setLastName(user.getLastname());
        userRepresentation.setEmail(user.getEmail());
        userRepresentation.setEnabled(true);
        userRepresentation.setEmailVerified(true);
        List<CredentialRepresentation> creds = new ArrayList<>();
        CredentialRepresentation cred = new CredentialRepresentation();
        cred.setTemporary(false);
        cred.setValue(user.getPassword());
        creds.add(cred);
        userRepresentation.setCredentials(creds);
        return userRepresentation;
    }
}
