package com.david.agenda_api.comercio.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ComercioRequestDto(
        @NotBlank(message = "El slug es obligatorio")
        @Size(max = 100, message = "El slug no puede superar los 100 caracteres")
        String slug,

        @NotBlank(message = "El nombre es obligatorio")
        @Size(max = 150, message = "El nombre no puede superar los 150 caracteres")
        String nombre,

        @Size(max = 20, message = "El teléfono no puede superar los 20 caracteres")
        String telefono,

        @Size(max = 255, message = "La dirección no puede superar los 255 caracteres")
        String direccion,

        @NotNull(message = "La duración del turno es obligatoria")
        @Min(value = 5, message = "La duración mínima de un turno son 5 minutos")
        Integer duracionTurnoMinutos,

        @NotNull(message = "La capacidad simultánea es obligatoria")
        @Min(value = 1, message = "La capacidad mínima es de 1 persona")
        Integer capacidadSimultanea,

        @NotNull(message = "El ID de usuario es obligatorio")
        Long usuarioId
)
 {
}
