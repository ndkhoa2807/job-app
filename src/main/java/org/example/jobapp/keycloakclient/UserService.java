package org.example.jobapp.keycloakclient;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.jobapp.config.KeycloakSecurityUtil;
import org.example.jobapp.dto.User;
import org.example.jobapp.exception.ResourceAlreadyExistsException;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.UserResource;
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
    private String REALM;

    private final KeycloakSecurityUtil keycloakSecurityUtil;

    public List<User> getUsers() {
        Keycloak keycloak = keycloakSecurityUtil.getKeycloakInstance();
        List<UserRepresentation> userRepresentations = keycloak
                .realm(REALM)
                .users().list();
        return mapUsers(userRepresentations);
    }

    public void registerUser(User user) {
        UserRepresentation userRepresentation = mapUserRep(user);
        Keycloak keycloak = keycloakSecurityUtil.getKeycloakInstance();
        List<String> roles = List.of("user");
        userRepresentation.setRealmRoles(roles);

        // check user already exist
        if (usernameAlreadyExists(user.getUsername())) {
            throw new ResourceAlreadyExistsException("Username already taken");
        }
        if (emailAlreadyExists(user.getEmail())) {
            throw new ResourceAlreadyExistsException("Email already taken");
        }

        keycloak.realm(REALM).users().create(userRepresentation);

    }

    public User updateUser(User user) {
        UserRepresentation userRepresentation = mapUserRep(user);
        Keycloak keycloak = keycloakSecurityUtil.getKeycloakInstance();
        keycloak.realm(REALM).users().get(user.getId()).update(userRepresentation);
        return user;
    }

    public void logout(User user) {
        UserRepresentation userRepresentation = mapUserRep(user);
        Keycloak keycloak = keycloakSecurityUtil.getKeycloakInstance();
        UserResource userResource = keycloak.realm(REALM).users().get(user.getId());
    }

    public User getUser(String id) {
        Keycloak keycloak = keycloakSecurityUtil.getKeycloakInstance();
        return mapUser(keycloak.realm(REALM).users().get(id).toRepresentation());
    }


    public void deleteUser(String id) {
        Keycloak keycloak = keycloakSecurityUtil.getKeycloakInstance();
        keycloak.realm(REALM).users().delete(id);
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

    private boolean usernameAlreadyExists(String username) {
        Keycloak keycloak = keycloakSecurityUtil.getKeycloakInstance();
        List<UserRepresentation> userRepresentations = keycloak.realm(REALM)
                .users().searchByUsername(username, true);
        return !userRepresentations.isEmpty();
    }

    private boolean emailAlreadyExists(String email) {
        Keycloak keycloak = keycloakSecurityUtil.getKeycloakInstance();
        List<UserRepresentation> userRepresentations = keycloak.realm(REALM)
                .users().searchByEmail(email, true);
        return !userRepresentations.isEmpty();
    }
}
