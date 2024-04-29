package spring;

import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;


@Configuration @EnableWebMvc
public class ServerConfig {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {

            @Override
            public void addCorsMappings(@NotNull CorsRegistry registry) {
                registry.addMapping("/**")
                    .allowedOriginPatterns("*")
                    .allowedHeaders("*")
                    .allowedMethods("GET", "POST", "PUT", "HEAD", "DELETE")
                    .allowCredentials(true);
            }
        };
    }
}
