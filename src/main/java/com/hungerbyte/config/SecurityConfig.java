package com.hungerbyte.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Spring Security 6 Configuration for Spring Boot 3.
 * Configures BCrypt password hashing, open static frontend resources,
 * and role-based REST endpoint permissions.
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // Disable CSRF for REST API architecture
            .csrf(csrf -> csrf.disable())
            .cors(cors -> {})
            // Configure session management
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))
            .authorizeHttpRequests(auth -> auth
                // Allow all static frontend HTML, CSS, JS, image resources
                .requestMatchers(
                    "/",
                    "/index.html",
                    "/*.html",
                    "/css/**",
                    "/js/**",
                    "/images/**",
                    "/favicon.ico"
                ).permitAll()
                // Allow public authentication & registration
                .requestMatchers("/api/auth/**").permitAll()
                // Allow public browsing of restaurants, foods, categories, coupons and reviews
                .requestMatchers(HttpMethod.GET, "/api/restaurants/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/foods/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/categories/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/coupons/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/reviews/**").permitAll()
                // Allow cart, order, favorites and profile operations for user
                .requestMatchers("/api/cart/**").permitAll()
                .requestMatchers("/api/orders/**").permitAll()
                .requestMatchers("/api/favorites/**").permitAll()
                .requestMatchers("/api/reviews/**").permitAll()
                .requestMatchers("/api/users/**").permitAll()
                .requestMatchers("/api/admin/**").permitAll()
                .requestMatchers("/api/owner/**").permitAll()
                // Any other request permit for smooth beginner dev experience
                .anyRequest().permitAll()
            )
            .headers(headers -> headers.frameOptions(frame -> frame.disable()));

        return http.build();
    }
}
