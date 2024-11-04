package com.bsn.book.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(
    info = @Info(
        contact = @Contact(
            name = "Hamza Rezgui", 
            email = "hamzarezgui.education@gmail.com"), 
            title = "Book API", version = "1.0", 
            description = "Open API documentation",
            license = @License(
                name = "License name", 
                url = "http://example.com/license"
            )
    ),
        servers = {
            @Server(
                url = "http://localhost:8088/api/v1",
                description = "Localhost server"
            ),
            @Server(
                url = "https://api.example.com",
                description = "Production server"
            )
        },
        security = {
            @SecurityRequirement(
                name = "bearerAuth"
            )
        }
    )
@SecurityScheme(
    name = "bearerAuth",
    description = "Bearer token authentication",
    scheme = "bearer",
    type = SecuritySchemeType.HTTP,
    bearerFormat = "JWT",
    in = SecuritySchemeIn.HEADER
)
public class OpenApiConfig {

}
