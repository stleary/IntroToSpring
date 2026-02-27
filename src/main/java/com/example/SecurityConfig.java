package com.example;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    /**
     * Defines the security filter chain.
     *
     * - GET requests are open to everyone (permitAll)
     * - POST, PUT, DELETE to /greeting require authentication
     * - CSRF is disabled (standard for stateless REST APIs)
     * - Sessions are stateless (no server-side session storage)
     * - JWT Bearer token validation via OAuth2 Resource Server
     *
     * Spring Security automatically configures a JwtDecoder using the
     * issuer-uri and jwk-set-uri from application.properties.
     * We don't need to create one manually.
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                // GET, POST, PUT, DELETE require authentication
                .antMatchers(HttpMethod.GET, "/greeting").authenticated()
                .antMatchers(HttpMethod.POST, "/greeting").authenticated()
                .antMatchers(HttpMethod.PUT, "/greeting").authenticated()
                .antMatchers(HttpMethod.DELETE, "/greeting").authenticated()
                // Everything else requires authentication
                .anyRequest().authenticated()
                .and()
                .oauth2ResourceServer()
                .jwt();
        return http.build();
    }
}

