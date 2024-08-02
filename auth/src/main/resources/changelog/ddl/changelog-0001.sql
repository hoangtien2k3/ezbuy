-- database changelog
-- Date: 2024-07-27

CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE user_otp
(
    id        UUID PRIMARY KEY   DEFAULT uuid_generate_v4(),
    type      VARCHAR   NOT NULL,
    email     VARCHAR   NOT NULL,
    otp       VARCHAR   NOT NULL,
    exp_time  TIMESTAMP NOT NULL,
    tries     INTEGER,
    status    INTEGER,
    create_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    create_by VARCHAR   NOT NULL,
    update_by VARCHAR   NOT NULL
);

CREATE TABLE individual
(
    id                UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    user_id           VARCHAR(36)  NOT NULL,
    username          VARCHAR(50)  NOT NULL,
    name              VARCHAR(255),
    code              TEXT,
    image             VARCHAR(500),
    gender            VARCHAR(10),
    birthday          TIMESTAMP,
    email             VARCHAR(200) NOT NULL,
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
    id          UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    user_id     VARCHAR(36),
    username    VARCHAR(50),
    hash_pwd    TEXT,
    status      SMALLINT,
    create_at   TIMESTAMP,
    create_by   VARCHAR(255),
    update_at   TIMESTAMP,
    update_by   VARCHAR(255),
    pwd_changed SMALLINT
);


