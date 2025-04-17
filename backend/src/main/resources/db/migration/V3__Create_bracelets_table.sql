CREATE TABLE bracelets (
    id BIGSERIAL PRIMARY KEY,
    uid_rfc VARCHAR(100) NOT NULL UNIQUE,
    user_id BIGINT NOT NULL,
    state VARCHAR(20) CHECK (state IN ('activa', 'bloqueada', 'desvinculada')) NOT NULL,
    link_date TIMESTAMP DEFAULT NOW(),
    CONSTRAINT fk_bracelet_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);
