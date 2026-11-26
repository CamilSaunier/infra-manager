
package com.inframanager.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI infraManagerOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Infra Manager API")
                        .description("REST API for managing network equipment and infrastructure.")
                        .version("1.0.0"));
    }
}
