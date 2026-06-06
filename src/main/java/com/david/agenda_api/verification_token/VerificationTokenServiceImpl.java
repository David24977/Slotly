package com.david.agenda_api.verification_token;

import com.david.agenda_api.verification_token.dto.VerificationTokenRequest;
import com.david.agenda_api.verification_token.dto.VerificationTokenResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VerificationTokenServiceImpl implements VerificationTokenService {

    private final VerificationTokenRepository tokenRepository;
    // private final ClienteService clienteService; // Lo necesitaremos para el alta/cotejo al vuelo
    // private final EmailService emailService; // Lo necesitaremos para disparar el correo electrónico

    @Override
    @Transactional
    public VerificationTokenResponse generateAndSendMagicLink(VerificationTokenRequest request) {
        String email = request.email();

        // 1. TODO: Cotejar con ClienteService. Si el cliente no existe con este email, se registra automáticamente.

        // 2. Limpiamos tokens anteriores activos para este email antes de generar uno nuevo
        tokenRepository.deleteByEmail(email);

        // 3. Generamos la cadena del token único
        String tokenStr = UUID.randomUUID().toString();

        // 4. Construimos y guardamos el token temporal con 15 minutos de vida
        VerificationToken verificationToken = VerificationToken.builder()
                .token(tokenStr)
                .email(email)
                .expiryDate(LocalDateTime.now().plusMinutes(15))
                .build();

        tokenRepository.save(verificationToken);

        // 5. TODO: Enviar el correo usando el emailService pasando el email y el tokenStr

        return new VerificationTokenResponse("Enlace mágico enviado con éxito a tu bandeja de entrada.", true);
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
}