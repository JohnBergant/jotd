package com.example.jotd.api;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Configuration for OpenAPI 3.0 documentation (Swagger UI).
 */
@Configuration
public class OpenAPIConfig {

    @Value("${spring.application.name}")
    private String applicationName;

    /**
     * Configures the OpenAPI documentation for the application.
     */
    @Bean
    public OpenAPI openAPI() {
        final String securitySchemeName = "apikey";
        
        return new OpenAPI()
                .info(apiInfo())
                .servers(servers())
                .components(new Components()
                        .addSecuritySchemes(securitySchemeName, new SecurityScheme()
                                .name(securitySchemeName)
                                .type(SecurityScheme.Type.APIKEY)
                                .in(SecurityScheme.In.HEADER)
                                .name("X-API-KEY")
                                .description("API Key passed in a header")))
                .addSecurityItem(new SecurityRequirement().addList(securitySchemeName));
    }

    /**
     * API information including title, description, version, and contact details.
     */
    private Info apiInfo() {
        return new Info()
                .title("Joke of the Day API")
                .description("REST API for managing jokes and retrieving the joke of the day")
                .version("1.0.0")
                .contact(new Contact()
                        .name("JOTD Team")
                        .email("jotd@example.com")
                        .url("https://github.com/yourusername/jotd"))
                .license(new License()
                        .name("MIT License")
                        .url("https://opensource.org/licenses/MIT"));
    }

    /**
     * Server information for different environments.
     */
    private List<Server> servers() {
        Server localServer = new Server()
                .url("http://localhost:8080")
                .description("Local development server");
        
        Server productionServer = new Server()
                .url("https://jotd.example.com")
                .description("Production server");
        
        return List.of(localServer, productionServer);
    }
}

