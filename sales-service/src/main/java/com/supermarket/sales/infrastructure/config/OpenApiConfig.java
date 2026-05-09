package com.supermarket.sales.infrastructure.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI salesServiceOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Sales Service API")
                        .description("Sale creation, refunds, and history")
                        .version("1.0.0"));
    }
}
