package com.david.agenda_api.comercio;

import com.david.agenda_api.comercio.dto.ComercioRequestDto;
import com.david.agenda_api.comercio.dto.ComercioResponseDto;
import com.david.agenda_api.comercio.dto.PatchComercioRequestDto;
import com.david.agenda_api.common.exception.BadRequestException;
import com.david.agenda_api.common.exception.ResourceNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class ComercioServiceImpl implements ComercioService {

    private final ComercioRepository comercioRepository;

    @Transactional
    @Override
    public ComercioResponseDto crearComercio(ComercioRequestDto request) {
        Comercio comercio = new Comercio();
        comercio.setSlug(request.slug());
        comercio.setNombre(request.nombre());
        comercio.setTelefono(request.telefono());
        comercio.setDireccion(request.direccion());
        comercio.setDuracionTurnoMinutos(request.duracionTurnoMinutos());
        comercio.setCapacidadSimultanea(request.capacidadSimultanea());

        comercioRepository.save(comercio);
        return ComercioResponseDto.fromEntity(comercio);
    }

    @Transactional(readOnly = true)
    @Override
    public ComercioResponseDto obtenerPorId(Long id) {
        Comercio comercio = comercioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Comercio con ID " + id + " no encontrado"));

        return ComercioResponseDto.fromEntity(comercio);
    }

    @Transactional(readOnly = true)
    @Override
    public ComercioResponseDto obtenerPorSlug(String slug) {
        Comercio comercio = comercioRepository.findBySlug(slug)
                .orElseThrow(() -> new ResourceNotFoundException("Comercio con slug '" + slug + "' no encontrado"));

        return ComercioResponseDto.fromEntity(comercio);
    }

    @Transactional
    @Override
    public ComercioResponseDto actualizarComercio(Long id, PatchComercioRequestDto request) {
        Comercio comercio = comercioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Comercio con ID " + id + " no encontrado"));

        if (request.slug() != null) {
            comercio.setSlug(request.slug());
        }
        if (request.nombre() != null) {
            comercio.setNombre(request.nombre());
        }
        if (request.telefono() != null) {
            comercio.setTelefono(request.telefono());
        }
        if (request.direccion() != null) {
            comercio.setDireccion(request.direccion());
        }
        if (request.duracionTurnoMinutos() != null) {
            comercio.setDuracionTurnoMinutos(request.duracionTurnoMinutos());
        }
        if (request.capacidadSimultanea() != null) {
            comercio.setCapacidadSimultanea(request.capacidadSimultanea());
        }
        if (request.activo() != null) {
            comercio.setActivo(request.activo());
        }

        comercioRepository.save(comercio);
        return ComercioResponseDto.fromEntity(comercio);
    }

    @Transactional
    @Override
    public ComercioResponseDto eliminarComercio(Long id) {
        Comercio comercio = comercioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Comercio con ID " + id + " no encontrado"));

        // Regla de negocio: Si está activo, no dejamos borrar directamente
        if (comercio.getActivo()) {
            throw new BadRequestException("No se puede eliminar un comercio activo. Primero debes desactivarlo.");
        }

        comercioRepository.delete(comercio);
        return ComercioResponseDto.fromEntity(comercio);
    }
}