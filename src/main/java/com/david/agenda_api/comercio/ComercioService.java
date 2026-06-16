package com.david.agenda_api.comercio;

import com.david.agenda_api.comercio.dto.ComercioRequestDto;
import com.david.agenda_api.comercio.dto.ComercioResponseDto;
import com.david.agenda_api.comercio.dto.PatchComercioRequestDto;

public interface ComercioService {
    ComercioResponseDto crearComercio(ComercioRequestDto request);
    ComercioResponseDto obtenerPorId(Long id);
    ComercioResponseDto obtenerPorSlug(String slug);
    ComercioResponseDto actualizarComercio(Long id, PatchComercioRequestDto request);
    ComercioResponseDto eliminarComercio(Long id);
}
