package com.entrevistador.gateway.config.security;

import com.entrevistador.gateway.config.properties.KeycloakJwtConverterProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class KeycloakJwtAuthenticationConverterTest {

    @Mock
    private KeycloakJwtConverterProperties properties;

    @Mock
    private KeycloakGrantedAuthoritiesConverter keycloakGrantedAuthoritiesConverter;

    @InjectMocks
    private KeycloakJwtAuthenticationConverter converter;

    private Jwt jwt;

    @BeforeEach
    void setUp() {
        jwt = Jwt.withTokenValue("token")
                .header("alg", "none")
                .claim("preferred_username", "testuser")
                .claim("sub", "user-subject")
                .build();
    }

    @Test
    void convert_ShouldReturnJwtAuthenticationToken_WithCorrectNameAndAuthorities() {
        when(properties.getUsernameAttribute()).thenReturn("preferred_username");
        when(keycloakGrantedAuthoritiesConverter.convert(jwt)).thenReturn(Collections.emptyList());

        AbstractAuthenticationToken result = converter.convert(jwt);

        assertEquals("testuser", result.getName());
        assertEquals(jwt, ((JwtAuthenticationToken) result).getToken());
        assertEquals(List.of(), result.getAuthorities());
    }

    @Test
    void convert_ShouldFallbackToSubject_WhenUsernameAttributeIsNull() {
        when(properties.getUsernameAttribute()).thenReturn(null);
        when(keycloakGrantedAuthoritiesConverter.convert(jwt)).thenReturn(Collections.emptyList());

        AbstractAuthenticationToken result = converter.convert(jwt);

        assertEquals("user-subject", result.getName());
    }

}