package com.david.agenda_api.verification_token.dto;

public record AuthResponse(
        String token,
        String mensaje
) {
}
