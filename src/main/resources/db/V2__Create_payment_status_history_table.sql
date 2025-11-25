CREATE TABLE payment_status_history (
    id BIGSERIAL PRIMARY KEY,
    payment_id BIGINT NOT NULL REFERENCES payments(id) ON DELETE CASCADE,
    status VARCHAR(20) NOT NULL,
    changed_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    changed_by VARCHAR(100),
    reason TEXT
);


CREATE INDEX idx_status_history_payment_id ON payment_status_history(payment_id);


COMMENT ON TABLE payment_status_history IS 'История документа';
COMMENT ON COLUMN payment_status_history.changed_by IS 'Кто изменил статус';
COMMENT ON COLUMN payment_status_history.changed_at IS 'Дата и время получение статуса';
COMMENT ON COLUMN payment_status_history.status IS 'Статус: PENDING, COMPLETED, FAILED, CANCELLED';
COMMENT ON COLUMN payment_status_history.reason IS 'Причина изменения';