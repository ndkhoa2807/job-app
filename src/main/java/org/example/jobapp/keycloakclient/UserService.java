package org.example.jobapp.keycloakclient;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.jobapp.config.KeycloakSecurityUtil;
import org.example.jobapp.dto.User;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.common.util.CollectionUtil;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    @Value("${security.realm}")
    private String realm;

    private final KeycloakSecurityUtil keycloakSecurityUtil;

    public List<User> getUsers() {
        Keycloak keycloak = keycloakSecurityUtil.getKeycloakInstance();
        List<UserRepresentation> userRepresentations = keycloak
                .realm(realm)
                .users().list();
        return mapUsers(userRepresentations);
    }

    public void registerUser(User user) {
        try {
            UserRepresentation userRepresentation = mapUserRep(user);
            Keycloak keycloak = keycloakSecurityUtil.getKeycloakInstance();
            keycloak.realm(realm).users().create(userRepresentation);
        } catch (Exception e) {
            log.error("Error register user", e);
        }
    }

    public User updateUser(User user) {
        UserRepresentation userRepresentation = mapUserRep(user);
        Keycloak keycloak = keycloakSecurityUtil.getKeycloakInstance();
        keycloak.realm(realm).users().get(user.getId()).update(userRepresentation);
        return user;
    }

    public User getUser(String id) {
        Keycloak keycloak = keycloakSecurityUtil.getKeycloakInstance();
        return mapUser(keycloak.realm(realm).users().get(id).toRepresentation());
    }


    public void deleteUser(String id) {
        Keycloak keycloak = keycloakSecurityUtil.getKeycloakInstance();
        keycloak.realm(realm).users().delete(id);
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
