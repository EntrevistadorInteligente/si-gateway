package com.entrevistador.gateway.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Set;

@Setter
@Getter
@ConfigurationProperties(prefix = "keycloak.jwt-converter")
public class KeycloakJwtConverterProperties {
    private Set<String> roleClaims = Set.of("realm_access.roles", "resource_access.account.roles");
    private String usernameAttribute;
}
