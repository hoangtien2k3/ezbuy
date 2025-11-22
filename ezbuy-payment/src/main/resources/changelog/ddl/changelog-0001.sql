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
    update_by       VARCHAR(100),
    CONSTRAINT fk_individual FOREIGN KEY (individual_id) REFERENCES individual(id)
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
    update_by                              VARCHAR(100),
    CONSTRAINT fk_individual_organization_permissions FOREIGN KEY (individual_organization_permissions_id) REFERENCES individual_organization_permissions(id)
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

CREATE TABLE option_set
(
    id          VARCHAR(36) NOT NULL PRIMARY KEY,
    code        VARCHAR(50) NOT NULL,
    name        VARCHAR(100) NOT NULL,
    status      INT NOT NULL,
    description VARCHAR(100) NOT NULL,
    create_at   TIMESTAMP DEFAULT CURRENT_TIMESTAMP NULL,
    update_at   TIMESTAMP NULL,
    create_by   VARCHAR(20) NULL,
    update_by   VARCHAR(20) NULL
);

CREATE TABLE option_set_value
(
    id            VARCHAR(36) NOT NULL PRIMARY KEY,
    option_set_id VARCHAR(36) NOT NULL,
    value         VARCHAR(200) NOT NULL,
    status        INT NOT NULL,
    description   VARCHAR(100) NOT NULL,
    CONSTRAINT fk_option_set FOREIGN KEY (option_set_id) REFERENCES option_set(id)
);

CREATE TABLE organization
(
    id             VARCHAR(36) NOT NULL PRIMARY KEY,
    name           VARCHAR(255) NULL,
    image          VARCHAR(500) NULL,
    business_type  VARCHAR(255) NULL,
    founding_date  TIMESTAMP NULL,
    email          VARCHAR(200) NULL,
    phone          VARCHAR(12) NULL,
    province_code  VARCHAR(5) NULL,
    district_code  VARCHAR(5) NULL,
    precinct_code  VARCHAR(5) NULL,
    street_block   VARCHAR(200) NULL,
    state          SMALLINT NULL,
    status         SMALLINT NULL,
    create_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP NULL,
    create_by      VARCHAR(255) NULL,
    update_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP NULL,
    update_by      VARCHAR(255) NULL,
    tax_department VARCHAR(255) NULL,
    org_type       VARCHAR(20) NULL,
    nation         VARCHAR(150) NULL
);

