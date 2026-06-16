package com.david.agenda_api.cliente.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ClienteRequestDto(
        @NotNull
        @Size(max = 100)
        String nombre,
        @NotNull
        @Email
        @Size(max = 150)
        String email
) {
}
