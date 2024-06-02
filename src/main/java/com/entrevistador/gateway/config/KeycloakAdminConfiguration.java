package com.entrevistador.gateway.config;

import com.entrevistador.gateway.config.properties.KeycloakProperties;
import lombok.NonNull;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.RealmResource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KeycloakAdminConfiguration {
    @Bean
    @NonNull
    Keycloak keycloak(@NonNull KeycloakProperties properties) {
        return KeycloakBuilder.builder()
                .serverUrl(properties.getServerUrl().toString())
                .realm(properties.getRealm())
                .grantType(OAuth2Constants.PASSWORD)
                .clientId(properties.getClient().getId())
                .clientSecret(properties.getClient().getSecret())
                .username(properties.getClient().getUsername())
                .password(properties.getClient().getPassword())
                .build();
    }

    @Bean
    @NonNull
    RealmResource keycloakRealmsResource(@NonNull Keycloak keycloak, @NonNull KeycloakProperties properties) {
        return keycloak.realm(properties.getRealm());
    }
}
