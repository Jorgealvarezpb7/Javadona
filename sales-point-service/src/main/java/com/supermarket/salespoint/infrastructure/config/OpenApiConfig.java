package com.supermarket.salespoint.infrastructure.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI salesPointOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Sales Point Service API")
                        .description("Store / Sucursal Management microservice")
                        .version("1.0.0"));
    }
}
