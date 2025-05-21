BEGIN;

CREATE TABLE IF NOT EXISTS users (
    id SERIAL PRIMARY KEY,
    login VARCHAR(25) UNIQUE NOT NULL,
    password_digest VARCHAR(64) NOT NULL,
    salt VARCHAR(64) NOT NULL
);

CREATE TABLE IF NOT EXISTS persons (
    id SERIAL PRIMARY KEY,
    weight DOUBLE PRECISION NOT NULL CONSTRAINT positive_weight CHECK (weight > 0),
    eye_color eye_color,
    hair_color hair_color,
    nationality country
);
CREATE TABLE IF NOT EXISTS coordinates
(
    id SERIAL PRIMARY KEY,
    x FLOAT  NOT NULL,
    y BIGINT NOT NULL,
    UNIQUE(x,y)
);
CREATE TABLE IF NOT EXISTS workers (
    id SERIAL PRIMARY KEY,
    name VARCHAR NOT NULL CONSTRAINT not_empty_name CHECK(length(name) > 0),
    coordinates INT REFERENCES coordinates(id) ON DELETE SET NULL,
    creation_date DATE DEFAULT NOW() NOT NULL,
    salary BIGINT NOT NULL CONSTRAINT positive_price CHECK (salary > 0),
    end_date DATE DEFAULT NOW() NOT NULL,
    position worker_position,
    status worker_status,
    person INT REFERENCES persons(id) ON DELETE SET NULL,
    creator_id INT NOT NULL REFERENCES users(id) ON DELETE CASCADE
);

END;