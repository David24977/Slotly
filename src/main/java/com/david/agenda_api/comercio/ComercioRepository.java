package com.david.agenda_api.comercio;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ComercioRepository extends JpaRepository<Comercio, Long> {

    Optional<Comercio> findBySlug(String slug);

    Optional<Comercio> findByUsuarioId(Long usuarioId);

    boolean existsBySlug(String slug);
}
