-- database changelog
-- Date: 2024-08-15

CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE rating
(
    id               VARCHAR(36) PRIMARY KEY,
    rating_type_code VARCHAR(100),
    target_id        VARCHAR(100),
    username         VARCHAR(100),
    cust_name        VARCHAR(255),
    rating           BIGINT,
    content          TEXT,
    rating_date      TIMESTAMP,
    has_image        INTEGER,
    has_video        INTEGER,
    status           INTEGER,
    state            VARCHAR(50),
    display_status   INTEGER,
    sum_rate_status  INTEGER,
    target_user      VARCHAR(255),
    create_at        TIMESTAMP,
    create_by        VARCHAR(255),
    update_at        TIMESTAMP,
    update_by        VARCHAR(255)
);

CREATE TABLE rating_count
(
    id               VARCHAR(36) PRIMARY KEY,
    rating_type_code VARCHAR(255),
    target_id        VARCHAR(255),
    number_rate      BIGINT,
    rating           FLOAT,
    max_rating       BIGINT,
    detail           TEXT,
    service_alias    VARCHAR(255)
);

CREATE TABLE rating_history
(
    id         VARCHAR(36) PRIMARY KEY,
    rating_id  VARCHAR(36) NOT NULL,
    rating_bf  BIGINT,
    rating_af  BIGINT,
    content_bf TEXT,
    content_af TEXT,
    approve_at TIMESTAMP,
    approve_by VARCHAR(255),
    state      VARCHAR(20),
    status     INTEGER,
    create_at  TIMESTAMP,
    create_by  VARCHAR(255),
    update_at  TIMESTAMP,
    update_by  VARCHAR(255)
);

CREATE TABLE rating_type
(
    id          VARCHAR(36) PRIMARY KEY,
    code        VARCHAR(255),
    name        VARCHAR(255),
    description TEXT,
    status      INTEGER,
    create_at   TIMESTAMP,
    create_by   VARCHAR(255),
    update_at   TIMESTAMP,
    update_by   VARCHAR(255)
);


