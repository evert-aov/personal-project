-- TABLA ROLES
CREATE TABLE roles (
    id BIGINT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    level INTEGER,
    is_active BOOLEAN DEFAULT TRUE
);

-- TABLA USERS
CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    code INTEGER UNIQUE NOT NULL,
    name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255),
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    is_active BOOLEAN DEFAULT TRUE
);

-- (Many to Many)
CREATE TABLE role_user (
    role_id BIGINT REFERENCES roles (id),
    user_id BIGINT REFERENCES users (id),
    PRIMARY KEY (role_id, user_id)
);
