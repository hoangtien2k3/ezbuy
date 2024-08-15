-- database changelog
-- Date: 2024-07-27

CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE user_otp
(
    id        VARCHAR(36) PRIMARY KEY,
    type      VARCHAR(30) NOT NULL,
    email     VARCHAR(50) NOT NULL,
    otp       VARCHAR(6)  NOT NULL,
    exp_time  TIMESTAMP   NOT NULL,
    tries     INTEGER,
    status    INTEGER,
    create_at TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_at TIMESTAMP,
    create_by VARCHAR(50),
    update_by VARCHAR(50)
);

CREATE TABLE individual
(
    id                VARCHAR(36) PRIMARY KEY,
    user_id           VARCHAR(36),
    username          VARCHAR(50),
    name              VARCHAR(100),
    code              TEXT,
    image             VARCHAR(500),
    gender            VARCHAR(10),
    birthday          TIMESTAMP,
    email             VARCHAR(50),
    email_account     VARCHAR(50),
    phone             VARCHAR(20),
    address           VARCHAR(255),
    province_code     VARCHAR(10),
    district_code     VARCHAR(10),
    precinct_code     VARCHAR(10),
    status            INTEGER,
    create_at         TIMESTAMP,
    create_by         VARCHAR(100),
    update_at         TIMESTAMP,
    update_by         VARCHAR(100),
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
    create_by   VARCHAR(100),
    update_at   TIMESTAMP,
    update_by   VARCHAR(100),
    pwd_changed INTEGER
);

CREATE TABLE action_log
(
    id         VARCHAR(36) PRIMARY KEY,
    user_id    VARCHAR(36) NOT NULL,
    username   VARCHAR(50) NOT NULL,
    ip         VARCHAR(45) NOT NULL,
    type       VARCHAR(20) NOT NULL,
    user_agent TEXT,
    create_at  TIMESTAMP   NOT NULL
);
CREATE INDEX idx_action_log_user_id ON action_log (user_id);
CREATE INDEX idx_action_log_create_at ON action_log (create_at);

CREATE TABLE individual_organization_permissions
(
    id              VARCHAR(36) PRIMARY KEY,
    individual_id   VARCHAR(36)  NOT NULL,
    organization_id VARCHAR(36)  NOT NULL,
    client_id       VARCHAR(255) NOT NULL,
    status          INTEGER      NOT NULL,
    create_at       TIMESTAMP,
    create_by       VARCHAR(100),
    update_at       TIMESTAMP,
    update_by       VARCHAR(100)
);

CREATE TABLE permission_policy
(
    id                                     VARCHAR(36) PRIMARY KEY,
    type                                   VARCHAR(100),
    value                                  VARCHAR(500),
    code                                   VARCHAR(255),
    description                            TEXT,
    keycloak_id                            VARCHAR(100),
    keycloak_name                          VARCHAR(100),
    policy_id                              VARCHAR(100),
    individual_organization_permissions_id VARCHAR(30),
    status                                 INTEGER,
    sso_id                                 VARCHAR(30),
    create_at                              TIMESTAMP,
    create_by                              VARCHAR(100),
    update_at                              TIMESTAMP,
    update_by                              VARCHAR(100)
);

CREATE TABLE user_profile
(
    id             VARCHAR(36) PRIMARY KEY,
    image          VARCHAR(255),
    company_name   VARCHAR(255),
    representative VARCHAR(255),
    phone          VARCHAR(50),
    tax_code       VARCHAR(50),
    tax_department VARCHAR(255),
    founding_date  DATE,
    business_type  VARCHAR(255),
    province_code  VARCHAR(50),
    district_code  VARCHAR(50),
    precinct_code  VARCHAR(50),
    street_block   VARCHAR(255),
    create_at      TIMESTAMP,
    create_by      VARCHAR(100),
    update_at      TIMESTAMP,
    update_by      VARCHAR(100),
    user_id        VARCHAR(255)
);


