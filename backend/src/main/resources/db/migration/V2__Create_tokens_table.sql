CREATE TABLE tokens (
    id SERIAL PRIMARY KEY,
    token TEXT UNIQUE NOT NULL,
    token_type TEXT CHECK (token_type IN ('BEARER')) NOT NULL DEFAULT 'BEARER',
    is_revoked BOOLEAN NOT NULL,
    is_expired BOOLEAN NOT NULL,
    user_id INTEGER NOT NULL,
    CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Crear un índice para mejorar la búsqueda de tokens
CREATE INDEX idx_tokens_token ON tokens(token);
