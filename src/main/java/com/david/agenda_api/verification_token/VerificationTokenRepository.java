package com.david.agenda_api.verification_token;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {

    // Para buscar el token que viene en la URL del enlace mágico
    Optional<VerificationToken> findByToken(String token);

    // Opcional: por si quieremos borrar los tokens viejos de un email antes de generar uno nuevo
    void deleteByEmail(String email);
}
