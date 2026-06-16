package com.david.agenda_api.comercio;

import com.david.agenda_api.comercio.dto.ComercioRequestDto;
import com.david.agenda_api.comercio.dto.ComercioResponseDto;
import com.david.agenda_api.comercio.dto.PatchComercioRequestDto;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/api/comercios")
public class ComercioController {
    private final ComercioService comercioService;

    @PostMapping
    public ComercioResponseDto crearComercio(@Valid @RequestBody ComercioRequestDto request){
        return comercioService.crearComercio(request);
    }

    @GetMapping("/{id}")
    public ComercioResponseDto obtenerPorId(@PathVariable Long id){
        return comercioService.obtenerPorId(id);
    }

    @GetMapping("/comercio_slug/{slug}")
    public ComercioResponseDto obtenerPorSlug(@PathVariable String slug){
        return comercioService.obtenerPorSlug(slug);
    }

    @PatchMapping("/{id}")
    public ComercioResponseDto actualizarComercio(@PathVariable Long id,
                                                  @Valid @RequestBody PatchComercioRequestDto request){
        return comercioService.actualizarComercio(id, request);
    }

    @DeleteMapping("/{id}")
    public ComercioResponseDto eliminarComercio(@PathVariable Long id){
        return comercioService.eliminarComercio(id);
    }

}
