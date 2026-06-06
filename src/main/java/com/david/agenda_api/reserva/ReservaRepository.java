package com.david.agenda_api.reserva;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface ReservaRepository extends JpaRepository<Reserva, Long> {

    // Vista del propietario — todas las reservas de un día
    List<Reserva> findByComercioIdAndFecha(Long comercioId, LocalDate fecha);

    // Vista del propietario — todas las reservas del comercio
    List<Reserva> findByComercioId(Long comercioId);

    // Validación de concurrencia — el corazón del negocio(Generarla por nombre-método sería posible,
    //  pero muy arriesgado por  mantenibilidad y fragilidad, mejor usar @Query)

    @Query("SELECT COUNT(r) FROM Reserva r " +
            "WHERE r.comercio.id = :comercioId " +
            "AND r.fecha = :fecha " +
            "AND r.horaInicio = :horaInicio " +
            "AND r.estado != 'CANCELADA'")
    int contarReservasEnSlot(
            @Param("comercioId") Long comercioId,
            @Param("fecha") LocalDate fecha,
            @Param("horaInicio") LocalTime horaInicio
    );
}
