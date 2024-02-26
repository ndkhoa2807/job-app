FROM keycloak/keycloak:23.0

COPY user-policy.jar /opt/keycloak/providers

