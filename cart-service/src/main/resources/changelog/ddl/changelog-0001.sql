-- database changelog
-- Date: 2024-10-06

CREATE TABLE cart
(
    id        VARCHAR(36) PRIMARY KEY,
    user_id   VARCHAR(36) NOT NULL,
    status    INTEGER     NOT NULL,
    create_at TIMESTAMP   NOT NULL DEFAULT NOW(),
    update_at TIMESTAMP            DEFAULT NOW(),
    create_by VARCHAR(255),
    update_by VARCHAR(255)
);

CREATE TABLE cart_item
(
    id                 VARCHAR(36) PRIMARY KEY,
    product_id         VARCHAR(36) NOT NULL,
    telecom_service_id VARCHAR(36),
    service_alias      VARCHAR(255),
    cart_id            VARCHAR(36) NOT NULL,
    quantity           BIGINT      NOT NULL,
    status             INTEGER     NOT NULL,
    create_at          TIMESTAMP   NOT NULL DEFAULT NOW(),
    update_at          TIMESTAMP            DEFAULT NOW(),
    create_by          VARCHAR(255),
    update_by          VARCHAR(255),
    FOREIGN KEY (cart_id) REFERENCES cart (id)
);

