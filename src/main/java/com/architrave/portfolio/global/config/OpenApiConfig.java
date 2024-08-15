package com.architrave.portfolio.global.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.tags.Tag;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.stream.Collectors;

@OpenAPIDefinition(
        info = @Info(
                title = "Architrave APIs",
                version = "1.0.0",
                description = "This is Architrave API document.",
                contact = @Contact(
                        name = "Architrave",
                        email = "nfs82young@gmail.com"
                ),
                license = @License(
                        name = "Architrave"
                )
        ),
        servers = {
                @Server(
                        description = "Local server",
                        url = "http://localhost:8080"
                ),
                @Server(
                        description = "product server",
                        url = "http://43.202.45.205:8080"
                )
        }
)
@SecurityScheme(
        name = "bearerAuth",
        description = "JWT auth description",
        scheme = "bearer",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        in = SecuritySchemeIn.HEADER
)
@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .addTagsItem(new Tag().name("1. Auth"))
                .addTagsItem(new Tag().name("2. Member"))
                .addTagsItem(new Tag().name("3. Project"))
                .addTagsItem(new Tag().name("4. ProjectElement"))
                .addTagsItem(new Tag().name("5. Work"))
                .addTagsItem(new Tag().name("6. LandingBox"));
    }

    @Bean
    public OpenApiCustomizer sortTagsAlphabetically() {
        return openApi -> {
            openApi.setTags(openApi.getTags().stream()
                    .sorted((tag1, tag2) -> tag1.getName().compareTo(tag2.getName()))
                    .collect(Collectors.toList()));
        };
    }
}
