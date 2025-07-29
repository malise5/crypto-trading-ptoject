package com.malise.trading.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfigurationSource;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, CorsConfigurationSource corsConfigurationSource) throws Exception {
        http.sessionManagement(
                management -> management.sessionCreationPolicy(
                        org.springframework.security.config.http.SessionCreationPolicy.STATELESS
                )
        ).authorizeHttpRequests(
                auth -> auth.requestMatchers("/api/**").authenticated()
                .anyRequest().permitAll()
        ).addFilterBefore(new JwtTokenValidator(), BasicAuthenticationFilter.class)
                .csrf(csrf -> csrf.disable()) // Disable CSRF protection
                .cors(cors -> cors.configurationSource(corsConfigurationSource()));
//
        return http.build();
    }

    private CorsConfigurationSource corsConfigurationSource() {
        // Implement your CORS configuration here
        // This is a placeholder, you should return a valid CorsConfigurationSource
        return null; // Replace with actual CORS configuration
    }
}
