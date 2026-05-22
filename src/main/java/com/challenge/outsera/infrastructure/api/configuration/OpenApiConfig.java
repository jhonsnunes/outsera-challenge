package com.challenge.outsera.infrastructure.api.configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    @Bean
    OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Outsera Challenge API")
                        .description("Serviço responsável pela análise dos dados de prêmios do cinema")
                        .version("1.0.0"));
    }
}
