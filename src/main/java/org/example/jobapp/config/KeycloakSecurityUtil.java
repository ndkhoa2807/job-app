package org.example.jobapp.config;

import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class KeycloakSecurityUtil {
    Keycloak keycloak;

    @Value("${security.server-url}")
    private String serverUrl;

    @Value("${security.realm}")
    private String realm;

    @Value("${security.client-id}")
    private String clientId;

    @Value("${security.grant-type}")
    private String grantType;

    @Value("${security.name}")
    private String username;

    @Value("${security.password}")
    private String password;

    public Keycloak getKeycloakInstance() {
        if (keycloak == null) {
            keycloak = KeycloakBuilder
                    .builder()
                    .serverUrl(serverUrl)
                    .realm(realm)
                    .clientId(clientId)
                    .grantType(grantType)
                    .username(username)
                    .password(password)
                    .build();
        }
        return keycloak;
    }

}
