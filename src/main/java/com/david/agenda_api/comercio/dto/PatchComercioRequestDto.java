package com.david.agenda_api.comercio.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

public record PatchComercioRequestDto(
        @Size(max = 100, message = "El slug no puede superar los 100 caracteres")
        String slug,

        @Size(max = 150, message = "El nombre no puede superar los 150 caracteres")
        String nombre,

        @Size(max = 20, message = "El teléfono no puede superar los 20 caracteres")
        String telefono,

        @Size(max = 255, message = "La dirección no puede superar los 255 caracteres")
        String direccion,

        @Min(value = 5, message = "La duración mínima de un turno son 5 minutos")
        Integer duracionTurnoMinutos,

        @Min(value = 1, message = "La capacidad mínima es de 1 persona")
        Integer capacidadSimultanea,

        Boolean activo
)
{
}

