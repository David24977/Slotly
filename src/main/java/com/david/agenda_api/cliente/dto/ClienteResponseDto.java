package com.david.agenda_api.cliente.dto;

import java.time.LocalDateTime;

public record ClienteResponseDto(
        Long id,
        String nombre,
        String email,
        String authProvider,
        LocalDateTime fechaRegistro,
        Boolean activo
) {
}
