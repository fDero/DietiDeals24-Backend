package spring;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import authentication.JwtAuthenticationFilter;
import authentication.JwtTokenProvider;

import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;


@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http = http.csrf( csrf -> csrf.disable() );
        http.exceptionHandling(customizer -> customizer.authenticationEntryPoint(
            new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
        );
        return http.authorizeHttpRequests(
            authorize -> authorize.requestMatchers(req -> 
                    req.getServletPath().equals("/login") || 
                    req.getServletPath().equals("/register/init") || 
                    req.getServletPath().equals("/register/confirm") ||
                    req.getServletPath().equals("/auctions") ||
                    req.getServletPath().equals("/categories") ||
                    req.getServletPath().equals("/error") 
                )
                .permitAll()
                .anyRequest()
                .authenticated()
            )
            .addFilterBefore(new JwtAuthenticationFilter(tokenProvider), UsernamePasswordAuthenticationFilter.class)
            .build();
    }
}