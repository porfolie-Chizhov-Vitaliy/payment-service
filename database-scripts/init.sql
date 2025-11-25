-- Основная таблица платежей
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

-- История статусов документов
CREATE TABLE payment_status_history (
    id BIGSERIAL PRIMARY KEY,
    payment_id BIGINT NOT NULL REFERENCES payments(id) ON DELETE CASCADE,
    status VARCHAR(20) NOT NULL,
    changed_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    changed_by VARCHAR(100), 
    reason TEXT                 
);


CREATE INDEX idx_payments_from_account ON payments(from_account);
CREATE INDEX idx_payments_status ON payments(status);
CREATE INDEX idx_payments_created_at ON payments(created_at);
CREATE INDEX idx_status_history_payment_id ON payment_status_history(payment_id);


COMMENT ON TABLE payment_status_history IS 'История документа';
COMMENT ON COLUMN payment_status_history.changed_by IS 'Кто изменил статус';
COMMENT ON COLUMN payment_status_history.changed_at IS 'Дата и время получение статуса';
COMMENT ON COLUMN payment_status_history.status IS 'Статус: PENDING, COMPLETED, FAILED, CANCELLED';
COMMENT ON COLUMN payment_status_history.reason IS 'Причина изменения';


COMMENT ON TABLE payments IS 'Платежи';
COMMENT ON COLUMN payments.from_account IS 'Счет отправителя';
COMMENT ON COLUMN payments.to_account IS 'Счет получателя';
COMMENT ON COLUMN payments.amount IS 'Сумма платежа';
COMMENT ON COLUMN payments.status IS 'Статус: PENDING, COMPLETED, FAILED, CANCELLED';