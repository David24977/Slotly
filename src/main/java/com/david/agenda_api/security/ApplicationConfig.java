package com.david.agenda_api.security;

import com.david.agenda_api.usuario.UsuarioRepository;
import com.david.agenda_api.cliente.ClienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class ApplicationConfig {

    private final UsuarioRepository usuarioRepository;
    private final ClienteRepository clienteRepository;

    // 1. Tu buscador unificado de usuarios/clientes
    @Bean
    public UserDetailsService userDetailsService() {
        return username -> {
            var usuario = usuarioRepository.findByEmail(username);
            if (usuario.isPresent()) {
                return usuario.get();
            }
            return clienteRepository.findByEmail(username)
                    .orElseThrow(() -> new UsernameNotFoundException("Usuario o Cliente no encontrado con email: " + username));
        };
    }

    // 2. El manejador de autenticación estándar (Spring le mete el provider automáticamente)
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    // 3. El encriptador de contraseñas
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}