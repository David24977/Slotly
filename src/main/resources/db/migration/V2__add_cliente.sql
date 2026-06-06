-- ============================================================
-- V2 — Entidad Cliente + relación con Reserva
-- ============================================================

CREATE TABLE clientes (
                          id              BIGSERIAL       PRIMARY KEY,
                          nombre          VARCHAR(100)    NOT NULL,
                          email           VARCHAR(150)    NOT NULL UNIQUE,
                          google_id       VARCHAR(100)    UNIQUE,
                          fecha_registro  TIMESTAMP       NOT NULL DEFAULT NOW(),
                          activo          BOOLEAN         NOT NULL DEFAULT TRUE
);

-- Relacionamos reservas con clientes (nullable — no todos los comercios lo exigen)
ALTER TABLE reservas
    ADD COLUMN cliente_id BIGINT,
    ADD CONSTRAINT fk_reserva_cliente
        FOREIGN KEY (cliente_id) REFERENCES clientes(id)
        ON DELETE SET NULL;

-- Índice para buscar cliente por google_id al hacer OAuth
CREATE INDEX idx_clientes_google_id ON clientes(google_id);

-- Índice para buscar reservas de un cliente concreto
CREATE INDEX idx_reservas_cliente ON reservas(cliente_id);