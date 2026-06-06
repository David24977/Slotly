package com.david.agenda_api.verification_token.dto;

public record VerificationTokenResponse(
        String mensaje,
        boolean exito
) {
}
