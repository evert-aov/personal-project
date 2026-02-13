ALTER TABLE working_days
    ADD COLUMN paid_amount DECIMAL(10, 2) NOT NULL DEFAULT 0;

ALTER TABLE transactions
    ADD COLUMN paid_amount DECIMAL(10, 2) NOT NULL DEFAULT 0;

ALTER TABLE transactions
    DROP COLUMN working_day_id;