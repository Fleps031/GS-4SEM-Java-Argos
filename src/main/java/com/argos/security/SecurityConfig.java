package com.argos.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .build();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .exceptionHandling(exception -> exception
                    .authenticationEntryPoint((request, response, authException) -> {
                        response.setStatus(401);
                        response.setContentType("application/json");
                        response.getWriter().write("{\"erro\": \"Não autorizado - Token inválido ou ausente\"}");
                    })
                    .accessDeniedHandler((request, response, accessDeniedException) -> {
                        response.setStatus(403);
                        response.setContentType("application/json");
                        response.getWriter().write("{\"erro\": \"Acesso negado\"}");
                    })
            )
            .authorizeHttpRequests(authz -> authz
                    // Endpoints protegidos (CREATE, UPDATE, DELETE)
                    .requestMatchers(HttpMethod.POST, "/api/missoes").authenticated()
                    .requestMatchers(HttpMethod.POST, "/api/leituras").authenticated()
                    .requestMatchers(HttpMethod.POST, "/api/alertas").authenticated()
                    
                    .requestMatchers(HttpMethod.PUT, "/api/missoes/**").authenticated()
                    .requestMatchers(HttpMethod.PATCH, "/api/missoes/**").authenticated()
                    .requestMatchers(HttpMethod.PATCH, "/api/alertas/**").authenticated()
                    
                    .requestMatchers(HttpMethod.DELETE, "/api/missoes/**").authenticated()
                    .requestMatchers(HttpMethod.DELETE, "/api/alertas/**").authenticated()
                    .requestMatchers(HttpMethod.DELETE, "/api/leituras/**").authenticated()

                    // Todos os outros endpoints são permitidos
                    .anyRequest().permitAll()
            )
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
