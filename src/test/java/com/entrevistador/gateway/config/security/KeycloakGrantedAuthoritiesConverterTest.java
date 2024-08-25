package com.entrevistador.gateway.config.security;

import com.entrevistador.gateway.config.properties.KeycloakJwtConverterProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class KeycloakGrantedAuthoritiesConverterTest {

    @Mock
    private KeycloakJwtConverterProperties properties;

    @InjectMocks
    private KeycloakGrantedAuthoritiesConverter converter;

    private Jwt jwt;

    @BeforeEach
    void setUp() {
        jwt = Jwt.withTokenValue("token")
                .header("alg", "none")
                .claim("realm_access", Map.of("roles", List.of("user", "admin")))
                .build();
    }

    @Test
    void convert_ShouldReturnGrantedAuthorities() {
        // Mock the properties to return the role claim paths
        List<String> roleClaims = List.of("realm_access.roles");
        when(properties.getRoleClaims()).thenReturn(new HashSet<>(roleClaims));  // Asegúrate de que el tipo coincida
        // Act
        Collection<GrantedAuthority> authorities = converter.convert(jwt);

        // Assert
        assertEquals(2, authorities.size());

        Set<String> expectedAuthorities = Set.of("ROLE_user", "ROLE_admin");
        Set<String> actualAuthorities = authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());

        assertEquals(expectedAuthorities, actualAuthorities);
    }
}