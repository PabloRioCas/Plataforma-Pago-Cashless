CREATE TABLE balance (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL UNIQUE,
    actual_balance DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    updated_in TIMESTAMP DEFAULT NOW(),
    CONSTRAINT fk_balance_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);
