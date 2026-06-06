-- 1. Añadimos la columna que le falta a la entidad Cliente
ALTER TABLE clientes ADD COLUMN auth_provider VARCHAR(20) NOT NULL DEFAULT 'GOOGLE';

-- 2. Quitamos la restricción unique de google_id si existía para que los de Hotmail puedan tener NULL
ALTER TABLE clientes DROP CONSTRAINT IF EXISTS clientes_google_id_key;