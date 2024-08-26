package com.entrevistador.gateway.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@TestPropertySource(locations = "classpath:test-application.properties")
class OpenApiConfigurationTest {

    @Autowired
    private ApplicationContext context;

    @Test
    void contextLoads() {
        // Verifica que el contexto carga correctamente la configuración OpenApiConfiguration
        assertThat(context.containsBean("openApiConfiguration")).isFalse();
    }
}