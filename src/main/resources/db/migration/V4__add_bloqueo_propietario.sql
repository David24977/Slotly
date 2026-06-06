-- V4__add_bloqueo_propietario.sql

ALTER TABLE reservas
    ADD COLUMN bloqueado_por_propietario BOOLEAN NOT NULL DEFAULT FALSE;

ALTER TABLE reservas
    ADD CONSTRAINT chk_cliente_o_bloqueo
        CHECK (
            bloqueado_por_propietario = TRUE
                OR cliente_id IS NOT NULL
            );