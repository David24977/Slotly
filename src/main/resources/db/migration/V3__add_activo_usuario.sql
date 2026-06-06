-- V3__add_activo_usuario.sql
ALTER TABLE usuarios
    ADD COLUMN activo BOOLEAN NOT NULL DEFAULT TRUE;