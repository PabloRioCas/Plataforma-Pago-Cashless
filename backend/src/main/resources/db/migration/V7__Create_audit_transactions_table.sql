CREATE TABLE audit_transactions (
    id BIGSERIAL PRIMARY KEY,
    transaction_id BIGINT NOT NULL,
    administrator_id BIGINT NOT NULL,
    comment VARCHAR(255),
    revision_date TIMESTAMP DEFAULT NOW(),
    CONSTRAINT fk_auditoria_transaccion FOREIGN KEY (transaction_id) REFERENCES transactions(id) ON DELETE CASCADE,
    CONSTRAINT fk_auditoria_admin FOREIGN KEY (administrator_id) REFERENCES administrators(id) ON DELETE CASCADE
);
