package org.example.jobapp.config;

import jakarta.servlet.Filter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.adapters.authorization.integration.jakarta.ServletPolicyEnforcerFilter;
import org.keycloak.adapters.authorization.spi.ConfigurationResolver;
import org.keycloak.adapters.authorization.spi.HttpRequest;
import org.keycloak.representations.adapters.config.PolicyEnforcerConfig;
import org.keycloak.util.JsonSerialization;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.oauth2.server.resource.web.authentication.BearerTokenAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;

import java.io.IOException;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@Slf4j
public class SecurityConfig {



    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable);
        http.addFilterAfter(createPolicyEnforceFilter(), BearerTokenAuthenticationFilter.class);
        http.sessionManagement(t -> t.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        return http.build();
    }

    private ServletPolicyEnforcerFilter createPolicyEnforceFilter() {
        return new ServletPolicyEnforcerFilter(new ConfigurationResolver() {
            @Override
            public PolicyEnforcerConfig resolve(HttpRequest httpRequest) {
                try {
                    return JsonSerialization
                            .readValue(getClass().getResourceAsStream("/policy-enforcer.json"), PolicyEnforcerConfig.class);
                } catch (IOException e) {
                    log.error("Error setting policy enforce from file");
                    throw new RuntimeException();
                }
            }
        });
    }


}
