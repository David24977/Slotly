package com.david.agenda_api.horario;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface HorarioRepository extends JpaRepository<Horario, Long> {

    List<Horario> findByComercioId(Long comercioId);

    List<Horario> findByComercioIdAndActivo(Long comercioId, Boolean activo);

    Optional<Horario> findByComercioIdAndDiaSemana(Long comercioId, DiaSemana diaSemana);
}

