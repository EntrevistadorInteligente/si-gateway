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

    @Test
    void extractAuthority_ShouldHandleMissingIntermediateLevel() {
        // Arrange
        when(properties.getRoleClaims()).thenReturn(Set.of("missing.level"));

        jwt = Jwt.withTokenValue("token")
                .header("alg", "none")
                .claim("missing", Map.of())  // No contiene "level"
                .build();

        // Act
        Collection<GrantedAuthority> authorities = converter.convert(jwt);

        // Assert
        assertTrue(authorities.isEmpty());
    }

    @Test
    void extractAuthority_ShouldHandlePathNotEndingInList() {
        // Arrange
        when(properties.getRoleClaims()).thenReturn(Set.of("realm_access.wrong_type"));

        // Proporcionar una estructura de datos válida
        jwt = Jwt.withTokenValue("token")
                .header("alg", "none")
                .claim("realm_access", Map.of("wrong_type", List.of("admin")))
                .build();

        // Act
        Collection<GrantedAuthority> authorities = converter.convert(jwt);

        // Assert
        assertEquals(1, authorities.size());
        assertTrue(authorities.stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_admin")));
    }

    @Test
    void extractAuthority_ShouldReturnListWhenPathIsSimpleClaim() {
        // Arrange
        when(properties.getRoleClaims()).thenReturn(Set.of("roles"));

        jwt = Jwt.withTokenValue("token")
                .header("alg", "none")
                .claim("roles", List.of("user", "admin"))
                .build();

        // Act
        Collection<GrantedAuthority> authorities = converter.convert(jwt);

        // Assert
        assertEquals(2, authorities.size());
        assertTrue(authorities.stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_user")));
        assertTrue(authorities.stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_admin")));
    }

    @Test
    void extractAuthority_ShouldReturnListWhenPathHasMultipleLevels() {
        // Arrange
        when(properties.getRoleClaims()).thenReturn(Set.of("realm_access.roles"));

        jwt = Jwt.withTokenValue("token")
                .header("alg", "none")
                .claim("realm_access", Map.of("roles", List.of("user", "admin")))
                .build();

        // Act
        Collection<GrantedAuthority> authorities = converter.convert(jwt);

        // Assert
        assertEquals(2, authorities.size());
        assertTrue(authorities.stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_user")));
        assertTrue(authorities.stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_admin")));
    }

    @Test
    void extractAuthority_ShouldHandlePartialPathCorrectly() {
        // Arrange
        when(properties.getRoleClaims()).thenReturn(Set.of("realm_access.partial.roles"));

        // Configura el JWT con una estructura que tiene una clave válida pero sin el camino esperado
        jwt = Jwt.withTokenValue("token")
                .header("alg", "none")
                .claim("realm_access", Map.of("partial", Map.of()))  // "roles" está ausente
                .build();

        // Act
        Collection<GrantedAuthority> authorities = converter.convert(jwt);

        // Assert
        assertTrue(authorities.isEmpty());
    }
}