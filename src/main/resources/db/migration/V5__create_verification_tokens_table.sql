-- V5 created table verification_tokens

CREATE TABLE verification_tokens (
                                     id BIGSERIAL PRIMARY KEY,
                                     token VARCHAR(255) NOT NULL UNIQUE,
                                     email VARCHAR(150) NOT NULL,
                                     expiry_date TIMESTAMP NOT NULL,
                                     used BOOLEAN NOT NULL DEFAULT FALSE
);

-- Indexamos el token porque se va a buscar constantemente al hacer clic en el email
CREATE INDEX idx_verification_tokens_token ON verification_tokens(token);