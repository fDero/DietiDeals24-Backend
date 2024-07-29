package spring;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import authentication.JwtAuthenticationFilter;
import authentication.JwtTokenManager;
import authentication.RequireJWT;

import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private JwtTokenManager tokenProvider;

    private Set<String> authorizationAwareEndpoints = new HashSet<>();

    @Autowired
    public SecurityConfig(
            JwtTokenManager tokenProvider,
            RequestMappingHandlerMapping requestMappingHandlerMapping) {
        this.tokenProvider = tokenProvider;
        Map<RequestMappingInfo, HandlerMethod> endpointPaths = requestMappingHandlerMapping.getHandlerMethods();
        for (Map.Entry<RequestMappingInfo, HandlerMethod> entry : endpointPaths.entrySet()) {
            if (entry.getValue().getMethod().getAnnotation(RequireJWT.class) != null) {
                Pattern r = Pattern.compile("\\[(.*?)\\]");
                Matcher m = r.matcher(entry.getKey().toString());
                if (m.find()) {
                    String group = m.group(1);
                    authorizationAwareEndpoints.add(group);
                }
            }
        }
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http = http.csrf(csrf -> csrf.disable());
        http.exceptionHandling(customizer -> customizer.authenticationEntryPoint(
                new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)));
        return http.cors(cors -> cors.configurationSource(corsConfigurationSource())).authorizeHttpRequests(
                authorize -> authorize
                        .requestMatchers(req -> !authorizationAwareEndpoints.contains(req.getServletPath()))
                        .permitAll()
                        .anyRequest()
                        .authenticated())
                .addFilterBefore(new JwtAuthenticationFilter(tokenProvider), UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration
                .setAllowedHeaders(Arrays.asList("Authorization", "Content-Type", "Accept", "Origin"));
        configuration.setAllowCredentials(false);
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}