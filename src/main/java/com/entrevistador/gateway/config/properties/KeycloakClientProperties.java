package com.entrevistador.gateway.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Setter
@Getter
@ConfigurationProperties(prefix = "keycloak.client")
public class KeycloakClientProperties {
    private String name;
    private String id;
    private String secret;
    private String username;
    private String password;
}
