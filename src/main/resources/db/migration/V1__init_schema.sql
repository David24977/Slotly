-- ============================================================
-- V1 — Schema inicial Slotly
-- ============================================================

-- USUARIOS (propietarios de comercios)
CREATE TABLE usuarios (
                          id                BIGSERIAL       PRIMARY KEY,
                          nombre            VARCHAR(100)    NOT NULL,
                          email             VARCHAR(150)    NOT NULL UNIQUE,
                          password          VARCHAR(255)    NOT NULL,
                          fecha_registro    TIMESTAMP       NOT NULL DEFAULT NOW()
);

-- COMERCIOS
CREATE TABLE comercios (
                           id                        BIGSERIAL       PRIMARY KEY,
                           slug                      VARCHAR(100)    NOT NULL UNIQUE,
                           nombre                    VARCHAR(150)    NOT NULL,
                           telefono                  VARCHAR(20),
                           direccion                 VARCHAR(255),
                           duracion_turno_minutos    INT             NOT NULL DEFAULT 30,
                           capacidad_simultanea      INT             NOT NULL DEFAULT 1,
                           activo                    BOOLEAN         NOT NULL DEFAULT TRUE,
                           fecha_creacion            TIMESTAMP       NOT NULL DEFAULT NOW(),
                           usuario_id                BIGINT          NOT NULL UNIQUE,

                           CONSTRAINT fk_comercio_usuario
                               FOREIGN KEY (usuario_id) REFERENCES usuarios(id)
                                   ON DELETE CASCADE
);

-- HORARIOS
CREATE TABLE horarios (
                          id              BIGSERIAL       PRIMARY KEY,
                          dia_semana      VARCHAR(10)     NOT NULL,
                          hora_apertura   TIME            NOT NULL,
                          hora_cierre     TIME            NOT NULL,
                          activo          BOOLEAN         NOT NULL DEFAULT TRUE,
                          comercio_id     BIGINT          NOT NULL,

                          CONSTRAINT fk_horario_comercio
                              FOREIGN KEY (comercio_id) REFERENCES comercios(id)
                                  ON DELETE CASCADE,

                          CONSTRAINT chk_dia_semana
                              CHECK (dia_semana IN (
                                                    'LUNES','MARTES','MIERCOLES',
                                                    'JUEVES','VIERNES','SABADO','DOMINGO'
                                  )),

                          CONSTRAINT chk_horario_coherente
                              CHECK (hora_cierre > hora_apertura)
);

-- RESERVAS
CREATE TABLE reservas (
                          id                  BIGSERIAL       PRIMARY KEY,
                          fecha               DATE            NOT NULL,
                          hora_inicio         TIME            NOT NULL,
                          duracion_minutos    INT             NOT NULL,
                          nombre_cliente      VARCHAR(100)    NOT NULL,
                          telefono_cliente    VARCHAR(20),
                          email_cliente       VARCHAR(150),
                          google_id           VARCHAR(100),
                          estado              VARCHAR(15)     NOT NULL DEFAULT 'PENDIENTE',
                          comercio_id         BIGINT          NOT NULL,

                          CONSTRAINT fk_reserva_comercio
                              FOREIGN KEY (comercio_id) REFERENCES comercios(id)
                                  ON DELETE CASCADE,

                          CONSTRAINT chk_estado_reserva
                              CHECK (estado IN ('PENDIENTE','CONFIRMADA','CANCELADA'))
);

-- ============================================================
-- ÍNDICES
-- ============================================================

-- Búsquedas de comercio por slug (ruta pública principal)
CREATE INDEX idx_comercios_slug       ON comercios(slug);

-- Horarios de un comercio por día
CREATE INDEX idx_horarios_comercio    ON horarios(comercio_id, dia_semana);

-- Validación de concurrencia: buscar reservas por comercio + fecha + hora
CREATE INDEX idx_reservas_slot
    ON reservas(comercio_id, fecha, hora_inicio);

-- Consulta del propietario: todas las reservas de su comercio por fecha
CREATE INDEX idx_reservas_fecha
    ON reservas(comercio_id, fecha);