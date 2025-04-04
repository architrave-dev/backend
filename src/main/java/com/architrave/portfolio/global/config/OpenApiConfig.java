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
                .addTagsItem(new Tag().name("00. AWS"))
                .addTagsItem(new Tag().name("01. Auth"))
                .addTagsItem(new Tag().name("02. Member"))
                .addTagsItem(new Tag().name("03. Project"))
                .addTagsItem(new Tag().name("04. ProjectInfo"))
                .addTagsItem(new Tag().name("05. ProjectElement"))
                .addTagsItem(new Tag().name("06. Work"))
                .addTagsItem(new Tag().name("07. WorkPropertyVisible"))
                .addTagsItem(new Tag().name("08. WorkDetail"))
                .addTagsItem(new Tag().name("09. Career"))
                .addTagsItem(new Tag().name("10. Billboard"))
                .addTagsItem(new Tag().name("11. MemberInfo"))
                .addTagsItem(new Tag().name("12. Contact"))
                .addTagsItem(new Tag().name("13. ContactPropertyVisible"))
                .addTagsItem(new Tag().name("14. Setting"));
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
