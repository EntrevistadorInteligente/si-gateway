package com.entrevistador.gateway;

import com.entrevistador.gateway.config.properties.KeycloakClientProperties;
import com.entrevistador.gateway.config.properties.KeycloakJwtConverterProperties;
import com.entrevistador.gateway.config.properties.KeycloakProperties;
import com.entrevistador.gateway.config.properties.SecurityProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;


@SpringBootApplication
@EnableDiscoveryClient
@SuppressWarnings("java:S116")
@EnableConfigurationProperties({
		KeycloakProperties.class,
		KeycloakClientProperties.class,
		KeycloakJwtConverterProperties.class,
		SecurityProperties.class,
})
public class GatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(GatewayApplication.class, args);
	}

}
