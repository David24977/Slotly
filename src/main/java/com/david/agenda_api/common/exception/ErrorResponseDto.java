package com.david.agenda_api.common.exception;

import java.time.LocalDateTime;
import java.util.Map;

public record ErrorResponseDto(
        LocalDateTime timestamp,
        int status,
        String error,
        String message,
        Map<String, String> errors // Para guardar los errores de validación de campos
) {
    // Constructor de conveniencia para errores simples (sin sub-errores de campos)
    public ErrorResponseDto(int status, String error, String message) {
        this(LocalDateTime.now(), status, error, message, null);
    }

}
