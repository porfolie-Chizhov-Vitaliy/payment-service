CREATE TABLE payments (
    id BIGSERIAL PRIMARY KEY,
    from_account VARCHAR(34) NOT NULL,
    to_account VARCHAR(34) NOT NULL,
    amount DECIMAL(15,2) NOT NULL CHECK (amount > 0),
    currency VARCHAR(3) NOT NULL DEFAULT 'RUB',
    status VARCHAR(20) NOT NULL DEFAULT 'PENDING',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    description TEXT
);


CREATE INDEX idx_payments_from_account ON payments(from_account);
CREATE INDEX idx_payments_status ON payments(status);
CREATE INDEX idx_payments_created_at ON payments(created_at);

COMMENT ON TABLE payments IS 'Платежи';
COMMENT ON COLUMN payments.from_account IS 'Счет отправителя';
COMMENT ON COLUMN payments.to_account IS 'Счет получателя';
COMMENT ON COLUMN payments.amount IS 'Сумма платежа';
COMMENT ON COLUMN payments.status IS 'Статус: PENDING, COMPLETED, FAILED, CANCELLED';