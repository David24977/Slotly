package com.david.agenda_api.comercio.dto;

import com.david.agenda_api.comercio.Comercio;

import java.time.LocalDateTime;

public record ComercioResponseDto(
        Long id,
        String slug,
        String nombre,
        String telefono,
        String direccion,
        Integer duracionTurnoMinutos,
        Integer capacidadSimultanea,
        Boolean activo,
        LocalDateTime fechaCreacion,
        Long usuarioId
) {
    public static ComercioResponseDto fromEntity(Comercio comercio) {
        return new ComercioResponseDto(
                comercio.getId(),
                comercio.getSlug(),
                comercio.getNombre(),
                comercio.getTelefono(),
                comercio.getDireccion(),
                comercio.getDuracionTurnoMinutos(),
                comercio.getCapacidadSimultanea(),
                comercio.getActivo(),
                comercio.getFechaCreacion(),
                comercio.getUsuario() != null ? comercio.getUsuario().getId() : null
        );
    }
}
