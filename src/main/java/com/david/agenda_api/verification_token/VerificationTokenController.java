package com.david.agenda_api.verification_token;

import com.david.agenda_api.verification_token.dto.AuthResponse;
import com.david.agenda_api.verification_token.dto.VerificationTokenRequest;
import com.david.agenda_api.verification_token.dto.VerificationTokenResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/magic-link")
@RequiredArgsConstructor
public class VerificationTokenController {

    private final VerificationTokenService tokenService;

    @PostMapping
    public ResponseEntity<VerificationTokenResponse> solicitarEnlaceMagico(
            @Valid @RequestBody VerificationTokenRequest request) {

        VerificationTokenResponse response = tokenService.generateAndSendMagicLink(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/verify")
    public ResponseEntity<?> verificarEnlaceMagico(@RequestParam String token) {
        try {
            // 1. Validamos el token y generamos el JWT definitivo
            String jwt = tokenService.validateTokenAndGenerateJwt(token);

            // 2. Devolvemos el JWT al cliente (en el futuro aquí puedes redirigir al frontend)
            return ResponseEntity.ok(new AuthResponse(jwt, "Login correcto"));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}