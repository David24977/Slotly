package com.david.agenda_api.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter filtroJwt;

    @Bean
    public SecurityFilterChain configuracionCadena(HttpSecurity http) throws Exception {
        http
                // Desactivamos CSRF porque no usamos cookies (vamos con tokens)
                .csrf(AbstractHttpConfigurer::disable)

                // Reglas de acceso a las rutas
                .authorizeHttpRequests(auth -> auth
                        // Endpoints públicos de autenticación (Login y Registro)
                        .requestMatchers("/api/magic-link/**").permitAll()

                        .requestMatchers("/api/comercios/**").permitAll()

                        // El resto de la API de Slotly requerirá estar autenticado
                        .anyRequest().authenticated()
                )

                // Indicamos que la API no guardará estados de sesión (Stateless)
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                // Añadimos nuestro filtro personalizado antes del filtro estándar
                .addFilterBefore(filtroJwt, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}