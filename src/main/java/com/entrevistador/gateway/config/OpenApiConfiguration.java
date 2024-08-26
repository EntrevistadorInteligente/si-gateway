package com.entrevistador.gateway.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(
        info = @Info(
                title = "API Gateway",
                description = "EntrevistadorInteligente APIs documentation",
                version = "1.0",
                contact = @Contact(
                        name = "EntrevistadorInteligente",
                        email = "entrevistadorinteligente@gmail.com"
                )
        ),
        servers = {
                @Server(
                        url = "http://localhost:${server.port}",
                        description = "Local ENV"
                ),
                @Server(
                        url = "http://api-gateway:${server.port}",
                        description = "Docker ENV"
                )
                ,
                @Server(
                        url = "https://gateway.pruebas-entrevistador-inteligente.site",
                        description = "Dev ENV"
                )
                ,
                @Server(
                        url = "https://gateway.pruebas-entrevistador-inteligente.site",
                        description = "Prod ENV"
                )
        }
)
@SuppressWarnings("all")
public class OpenApiConfiguration {
}


