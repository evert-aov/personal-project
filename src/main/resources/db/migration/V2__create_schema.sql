CREATE TYPE day_status AS ENUM ('FULL_DAY', 'HALF_DAY', 'NO_DAY');
CREATE TYPE transaction_type AS ENUM ('LOAN', 'RECEIVED', 'EXPENSE', 'INCOME');

CREATE TABLE working_days
(
    id          BIGSERIAL PRIMARY KEY,
    user_id     BIGINT NOT NULL,
    date        DATE NOT NULL,
    status      day_status NOT NULL,
    amount_won  DOUBLE PRECISION DEFAULT 0,

    FOREIGN KEY (user_id) REFERENCES users (id),
    UNIQUE (user_id, date)
);

CREATE TABLE transactions
(
    id             BIGSERIAL PRIMARY KEY,
    user_id        BIGINT NOT NULL,
    working_day_id BIGINT,

    description    VARCHAR(255),
    date           DATE NOT NULL,
    type           transaction_type NOT NULL,
    amount         DOUBLE PRECISION NOT NULL,

    FOREIGN KEY (user_id) REFERENCES users (id),
    FOREIGN KEY (working_day_id) REFERENCES working_days (id)
);