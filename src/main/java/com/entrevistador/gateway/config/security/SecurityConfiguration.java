package com.entrevistador.gateway.config.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import reactor.core.publisher.Mono;

@Slf4j
@Configuration
@EnableWebFluxSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {
    private final KeycloakLogoutHandler keycloakLogoutHandler;

    @Bean
    public SecurityWebFilterChain securityFilterChain(@NonNull ServerHttpSecurity httpSecurity) {
        return httpSecurity
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .formLogin(ServerHttpSecurity.FormLoginSpec::disable)
                .oauth2ResourceServer(serverSpec -> serverSpec.jwt(Customizer.withDefaults()))

                .logout(logoutHandler -> logoutHandler
                        .logoutHandler(keycloakLogoutHandler)
                        .logoutUrl("/logout")
                        .logoutSuccessHandler((exchange, authentication) -> {
                            log.debug("{}", authentication);
                            return Mono.empty();
                        })
                )

                .authorizeExchange(exchangeSpec -> exchangeSpec
                        .pathMatchers(
                                "/webjars/**",
                                "/v3/api-docs/**",
                                "/swagger-ui.html",
                                "/swagger-ui/**",
                                "/swagger-resources",
                                "/swagger-resources/**",
                                "/configuration/ui",
                                "/configuration/security",
                                "/actuator/**",
                                "/error",
                                "/sso/logout",
                                "/logout",
                                "/api/orquestador/v1/entrevistador/public/**",
                                "/api/notificaciones/v1/eventos/subscribe/**",
                                "/api/administrador-entrevista/v1/muestra/**"
                        )
                        .permitAll()
                        .anyExchange().authenticated()
                )
                .build();
    }
}
