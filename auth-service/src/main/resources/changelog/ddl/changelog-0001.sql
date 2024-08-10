-- database changelog
-- Date: 2024-07-27

CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE user_otp
(
    id        VARCHAR(36) PRIMARY KEY,
    type      VARCHAR   NOT NULL,
    email     VARCHAR   NOT NULL,
    otp       VARCHAR   NOT NULL,
    exp_time  TIMESTAMP NOT NULL,
    tries     INTEGER,
    status    INTEGER,
    create_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_at TIMESTAMP,
    create_by VARCHAR,
    update_by VARCHAR
);

CREATE TABLE individual
(
    id                VARCHAR(36) PRIMARY KEY,
    user_id           VARCHAR(36),
    username          VARCHAR(50),
    name              VARCHAR(255),
    code              TEXT,
    image             VARCHAR(500),
    gender            VARCHAR(10),
    birthday          TIMESTAMP,
    email             VARCHAR(200),
    email_account     VARCHAR(200),
    phone             VARCHAR(20),
    address           VARCHAR(255),
    province_code     VARCHAR(5),
    district_code     VARCHAR(5),
    precinct_code     VARCHAR(5),
    status            INTEGER,
    create_at         TIMESTAMP,
    create_by         VARCHAR(255),
    update_at         TIMESTAMP,
    update_by         VARCHAR(255),
    position_code     VARCHAR(20),
    password_change   BOOLEAN,
    probation_day     TIMESTAMP,
    start_working_day TIMESTAMP
);

CREATE TABLE user_credential
(
    id          VARCHAR(36) PRIMARY KEY,
    user_id     VARCHAR(36),
    username    VARCHAR(50),
    hash_pwd    TEXT,
    status      INTEGER,
    create_at   TIMESTAMP,
    create_by   VARCHAR(255),
    update_at   TIMESTAMP,
    update_by   VARCHAR(255),
    pwd_changed INTEGER
);


