package com.entrevistador.gateway.config.security;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.resource.RealmResource;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimAccessor;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.logout.ServerLogoutHandler;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class KeycloakLogoutHandler implements ServerLogoutHandler {
    private final RealmResource realmResource;

    @Override
    public Mono<Void> logout(WebFilterExchange exchange, Authentication authentication) {
        return Mono.just(authentication)
                .map(Authentication::getPrincipal)
                .cast(Jwt.class)
                .map(JwtClaimAccessor::getSubject)
                .flatMap(this::logoutFromKeycloak);
    }

    private Mono<Void> logoutFromKeycloak(@NonNull String subject) {
        realmResource.users().get(subject).logout();
        return Mono.empty();
    }
}
