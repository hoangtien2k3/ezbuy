-- database changelog
-- Date: 2024-07-27

CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE user_otp
(
    id        UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    type      VARCHAR   NOT NULL,
    email     VARCHAR   NOT NULL,
    otp       VARCHAR   NOT NULL,
    exp_time  TIMESTAMP NOT NULL,
    tries     INTEGER            DEFAULT 0 CHECK (tries >= 0),
    status    INTEGER   NOT NULL CHECK (status IN (0, 1)),
    create_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    create_by VARCHAR   NOT NULL,
    update_by VARCHAR   NOT NULL
);

