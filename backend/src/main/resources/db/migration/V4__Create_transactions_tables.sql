CREATE TABLE transactions (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    type VARCHAR(10) CHECK (type IN ('RECHARGE', 'PAYMENT')) NOT NULL,
    qty DECIMAL(10,2) NOT NULL,
    description VARCHAR(255),
    date TIMESTAMP DEFAULT NOW(),
    origin VARCHAR(100),
    CONSTRAINT fk_transaction_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);
