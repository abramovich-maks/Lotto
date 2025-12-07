package com.lotto.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.oas.annotations.EnableOpenApi;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.HttpAuthenticationScheme;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.Collections;
import java.util.List;

@Configuration
@EnableOpenApi
public class SwaggerConfig {

    @Bean
    public Docket api() {

        HttpAuthenticationScheme bearerAuth = HttpAuthenticationScheme.JWT_BEARER_BUILDER
                .name("bearerAuth")
                .build();

        return new Docket(DocumentationType.OAS_30)
                .apiInfo(apiInfo())
                .securitySchemes(Collections.singletonList(bearerAuth))
                .securityContexts(Collections.singletonList(securityContext()))
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.lotto"))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Lotto API")
                .description("API documentation for Lotto")
                .version("1.0")
                .build();
    }

    private SecurityContext securityContext() {
        return SecurityContext.builder()
                .securityReferences(defaultAuth())
                .operationSelector(o -> true)
                .build();
    }

    private List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope =
                new AuthorizationScope("global", "accessEverything");
        return Collections.singletonList(
                new SecurityReference("bearerAuth",
                        new AuthorizationScope[]{authorizationScope})
        );
    }
}
