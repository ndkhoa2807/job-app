package org.example.jobapp.keycloakclient;

import lombok.RequiredArgsConstructor;
import org.example.jobapp.config.KeycloakSecurityUtil;
import org.example.jobapp.dto.Role;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.common.util.CollectionUtil;
import org.keycloak.representations.idm.RoleRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleService {
    private final KeycloakSecurityUtil keycloakSecurityUtil;

    @Value("${realm}")
    private String realm;

    public List<Role> getRoles() {
        Keycloak keycloak = keycloakSecurityUtil.getKeycloakInstance();
        List<RoleRepresentation> roleRepresentations = keycloak
                .realm(realm)
                .roles()
                .list();
        return mapRoles(roleRepresentations);
    }

    public Role getRole(String roleName) {
        Keycloak keycloak = keycloakSecurityUtil.getKeycloakInstance();
        RoleRepresentation roleRepresentation = keycloak.realm(roleName).roles().get(roleName).toRepresentation();
        return mapRole(roleRepresentation);
    }

    public Role createRole(Role role) {
        RoleRepresentation roleRepresentation = mapRoleRep(role);
        Keycloak keycloak = keycloakSecurityUtil.getKeycloakInstance();
        keycloak.realm(realm).roles().create(roleRepresentation);
        return role;
    }

    public void deleteRole(String roleName) {
        Keycloak keycloak = keycloakSecurityUtil.getKeycloakInstance();
        keycloak.realm(realm).roles().deleteRole(roleName);
    }

    private Role mapRole(RoleRepresentation roleRepresentation) {
        Role role = new Role();
        role.setId(roleRepresentation.getId());
        role.setName(roleRepresentation.getName());
        return role;
    }

    private List<Role> mapRoles(List<RoleRepresentation> roleRepresentations) {
        List<Role> roles = new ArrayList<>();
        if (CollectionUtil.isNotEmpty(roleRepresentations)) {
            roleRepresentations.forEach(roleRepresentation -> roles.add(mapRole(roleRepresentation)));
        }
        return roles;
    }
    private RoleRepresentation mapRoleRep(Role role) {
        RoleRepresentation roleRep = new RoleRepresentation();
        roleRep.setName(role.getName());
        return roleRep;
    }


}
