-- database changelog
-- Date: 2024-10-06

CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE cart
(
    id        UUID PRIMARY KEY,
    user_id   UUID      NOT NULL,
    status    INTEGER   NOT NULL,
    create_at TIMESTAMP NOT NULL DEFAULT NOW(),
    update_at TIMESTAMP          DEFAULT NOW(),
    create_by VARCHAR(255),
    update_by VARCHAR(255)
);

CREATE TABLE cart_item
(
    id                 UUID PRIMARY KEY,
    product_id         UUID      NOT NULL,
    telecom_service_id UUID,
    service_alias      VARCHAR(255),
    cart_id            UUID      NOT NULL,
    quantity           BIGINT    NOT NULL,
    status             INTEGER   NOT NULL,
    create_at          TIMESTAMP NOT NULL DEFAULT NOW(),
    update_at          TIMESTAMP          DEFAULT NOW(),
    create_by          VARCHAR(255),
    update_by          VARCHAR(255),
    FOREIGN KEY (cart_id) REFERENCES cart (id)
);

