package spring;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;

@OpenAPIDefinition
@Configuration
public class OpenAPIConfig {
    
    @Bean 
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
            .info(new Info().title("Dieti-Deals-24 Backend")
            .version("1.0")
            .description("INGSW 2023/2024")
        );
    }
}
