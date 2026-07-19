-- Postgres native enum types (day_status, transaction_type) don't accept the
-- VARCHAR parameter Hibernate sends for @Enumerated(EnumType.STRING) fields
-- without an explicit cast, so every insert/update on these columns failed
-- with: "column is of type X but expression is of type character varying"
-- (SQLState 42804). Switch both to plain VARCHAR + CHECK, matching how
-- status-like columns are handled elsewhere in this project.

ALTER TABLE working_days
    ALTER COLUMN status TYPE VARCHAR(9) USING status::text;
ALTER TABLE working_days
    ADD CONSTRAINT working_days_status_check CHECK (status IN ('FULL_DAY', 'HALF_DAY', 'NO_DAY'));

ALTER TABLE transactions
    ALTER COLUMN type TYPE VARCHAR(10) USING type::text;
ALTER TABLE transactions
    ADD CONSTRAINT transactions_type_check CHECK (type IN ('LOAN', 'RECEIVED', 'EXPENSE', 'INCOME'));

DROP TYPE day_status;
DROP TYPE transaction_type;
