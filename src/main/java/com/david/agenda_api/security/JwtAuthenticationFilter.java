package com.david.agenda_api.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {


    private final JwtService jwtService;
    private final UserDetailsService uDetailsService;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        final String cabecera = request.getHeader("Authorization");
        final String token;
        final String correoUsuario;

        // Si no viene la cabecera o no empieza con el formato correcto, al siguiente filtro
        if (cabecera == null || !cabecera.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        token = cabecera.substring(7);
        // Usamos nuestro servicio para sacar el identificador (email)
        correoUsuario = jwtService.extractUsername(token);

        // Si hay usuario y el sistema no lo ha validado todavía en este hilo de ejecución...
        if (correoUsuario != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            // Llama a nuestro buscador doble (el de Usuarios y Clientes)
            UserDetails datosUser = this.uDetailsService.loadUserByUsername(correoUsuario);

            // Validamos que el token coincida con el usuario y no esté caducado
            if (jwtService.isTokenValid(token, datosUser)) {
                UsernamePasswordAuthenticationToken tokenVerificado = new UsernamePasswordAuthenticationToken(
                        datosUser,
                        null,
                        datosUser.getAuthorities()
                );

                tokenVerificado.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // Lo registramos en el contexto interno
                SecurityContextHolder.getContext().setAuthentication(tokenVerificado);
            }
        }

        filterChain.doFilter(request, response);
    }
}