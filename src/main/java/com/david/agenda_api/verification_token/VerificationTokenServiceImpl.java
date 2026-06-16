package com.david.agenda_api.verification_token;

import com.david.agenda_api.cliente.ClienteRepository;
import com.david.agenda_api.cliente.ClienteService;
import com.david.agenda_api.email.EmailService;
import com.david.agenda_api.security.JwtService;
import com.david.agenda_api.verification_token.dto.VerificationTokenRequest;
import com.david.agenda_api.verification_token.dto.VerificationTokenResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VerificationTokenServiceImpl implements VerificationTokenService {

    private final VerificationTokenRepository tokenRepository;
    private final ClienteService clienteService; // Lo necesitamos para el alta/cotejo al vuelo
    private final EmailService emailService;
    private final JwtService jwtService;
    private final ClienteRepository clienteRepository;// Lo necesitamos para disparar el correo electrónico

    @Override
    @Transactional
    public VerificationTokenResponse generateAndSendMagicLink(VerificationTokenRequest request) {
        String email = request.email();

        // 1. Buscamos al cliente por email. Si no existe, se registra automáticamente al vuelo.
        // El ID de Postgres se genera aquí dentro si es nuevo.
        clienteService.registrarOSiExisteObtener(email);

        // 2. Limpiamos tokens anteriores activos para este email
        tokenRepository.deleteByEmail(email);

        // 3. Generamos la cadena del token único
        String tokenStr = UUID.randomUUID().toString();

        // 4. Construimos y guardamos el token temporal
        VerificationToken verificationToken = VerificationToken.builder()
                .token(tokenStr)
                .email(email)
                .expiryDate(LocalDateTime.now().plusMinutes(15))
                .build();

        tokenRepository.save(verificationToken);

        // 5. TODO: Enviar el correo usando el emailService pasando el email y el tokenStr

        emailService.enviarMagicLink(email, tokenStr);

        // 6. El return final del método, que devuelve el DTO de respuesta
        return new VerificationTokenResponse("Enlace enviado con éxito a tu bandeja de entrada.", true);
    }

    @Override
    public VerificationToken validateToken(String tokenStr) {
        VerificationToken verificationToken = tokenRepository.findByToken(tokenStr)
                .orElseThrow(() -> new RuntimeException("El enlace mágico no es válido."));

        //Boolean.TRUE.equals(verificationToken.getUsed()) para evitar posibles NullPointerException,
        // aunque en teoria no debría ocurrir al poner nullable=false, a veces ocurre
        if (Boolean.TRUE.equals(verificationToken.getUsed())) {
            throw new RuntimeException("Este enlace ya ha sido utilizado.");
        }

        if (verificationToken.isExpired()) {
            throw new RuntimeException("El enlace mágico ha caducado.");
        }

        return verificationToken;
    }

    @Override
    @Transactional
    public void invalidateToken(VerificationToken token) {
        token.setUsed(true);
        tokenRepository.save(token);
    }

    @Override
    @Transactional
    public String validateTokenAndGenerateJwt(String tokenStr) {
        // 1. Reutilizamos este método para buscar y validar expiración/uso
        VerificationToken verificationToken = validateToken(tokenStr);

        // 2. Reutilizamos el método para marcarlo como usado (Used = true)
        invalidateToken(verificationToken);

        // 3. Obtenemos el email del token
        String email = verificationToken.getEmail();

        // 4. Buscamos la entidad real en la Base de Datos (que sí es un UserDetails)
        UserDetails userDetails = clienteRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado en el sistema"));

        // 5. Generamos el JWT definitivo para React
        // Nota: Inyecta tu JwtService/TokenProvider en el constructor si aún no lo tienes
        return jwtService.generateToken(userDetails);
    }
}