-- database: customer

CREATE TABLE shop_user
(
    id                       SERIAL PRIMARY KEY,
    customer_id              INT          NOT NULL,
    username                 VARCHAR(255),
    username_canonical       VARCHAR(255),
    enabled                  SMALLINT     NOT NULL DEFAULT 0,
    salt                     VARCHAR(255) NOT NULL,
    password                 VARCHAR(255),
    last_login               TIMESTAMP,
    password_reset_token     VARCHAR(255),
    password_requested_at    TIMESTAMP,
    email_verification_token VARCHAR(255),
    verified_at              TIMESTAMP,
    locked                   SMALLINT     NOT NULL DEFAULT 0,
    expires_at               TIMESTAMP,
    credentials_expire_at    TIMESTAMP,
    roles                    VARCHAR(50)  NOT NULL,
    email                    VARCHAR(255),
    email_canonical          VARCHAR(255),
    created_at               TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at               TIMESTAMP
);

ALTER TABLE shop_user
    ADD CONSTRAINT shop_user_customer_id_unique UNIQUE (customer_id);

ALTER TABLE shop_user
    ADD CONSTRAINT shop_user_password_reset_token_unique UNIQUE (password_reset_token);

ALTER TABLE shop_user
    ADD CONSTRAINT shop_user_email_verification_token_unique UNIQUE (email_verification_token);

CREATE TABLE user_oauth
(
    id            SERIAL PRIMARY KEY,
    user_id       INT,
    provider      VARCHAR(255) NOT NULL,
    identifier    VARCHAR(255) NOT NULL,
    access_token  TEXT,
    refresh_token TEXT
);

ALTER TABLE user_oauth
    ADD CONSTRAINT user_oauth_user_id_provider_unique UNIQUE (user_id, provider);

CREATE INDEX user_oauth_user_id_index ON user_oauth (user_id);

CREATE TABLE address
(
    id            SERIAL PRIMARY KEY,
    customer_id   INT,
    first_name    VARCHAR(255) NOT NULL,
    last_name     VARCHAR(255) NOT NULL,
    phone_number  VARCHAR(255),
    street        VARCHAR(255) NOT NULL,
    company       VARCHAR(255),
    city          VARCHAR(255) NOT NULL,
    postcode      VARCHAR(255) NOT NULL,
    created_at    TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at    TIMESTAMP,
    country_code  VARCHAR(255) NOT NULL,
    province_code VARCHAR(255),
    province_name VARCHAR(255)
);

CREATE INDEX address_customer_id_index ON address (customer_id);

CREATE TABLE customer
(
    id                       SERIAL PRIMARY KEY,
    customer_group_id        INT,
    default_address_id       INT,
    email                    VARCHAR(255) NOT NULL,
    email_canonical          VARCHAR(255) NOT NULL,
    first_name               VARCHAR(255),
    last_name                VARCHAR(255),
    birthday                 TIMESTAMP,
    gender                   VARCHAR(20)  NOT NULL DEFAULT 'OTHER',
    created_at               TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at               TIMESTAMP,
    phone_number             VARCHAR(255),
    subscribed_to_newsletter BOOLEAN      NOT NULL DEFAULT false
);

CREATE INDEX customer_customer_group_id_index ON customer (customer_group_id);
ALTER TABLE customer
    ADD CONSTRAINT customer_default_address_id_unique UNIQUE (default_address_id);
ALTER TABLE customer
    ADD CONSTRAINT customer_email_unique UNIQUE (email);
ALTER TABLE customer
    ADD CONSTRAINT customer_email_canonical_unique UNIQUE (email_canonical);
CREATE INDEX customer_created_at_index ON customer (created_at);

CREATE TABLE customer_group
(
    id   SERIAL PRIMARY KEY,
    code VARCHAR(255) NOT NULL,
    name VARCHAR(255) NOT NULL
);

ALTER TABLE customer_group
    ADD CONSTRAINT customer_group_code_unique UNIQUE (code);

-- foreign key
ALTER TABLE user_oauth
    ADD CONSTRAINT user_oauth_user_id_foreign FOREIGN KEY (user_id) REFERENCES shop_user (id);
ALTER TABLE shop_user
    ADD CONSTRAINT shop_user_customer_id_foreign FOREIGN KEY (customer_id) REFERENCES customer (id);
ALTER TABLE address
    ADD CONSTRAINT address_customer_id_foreign FOREIGN KEY (customer_id) REFERENCES customer (id);
ALTER TABLE customer
    ADD CONSTRAINT customer_customer_group_id_foreign FOREIGN KEY (customer_group_id) REFERENCES customer_group (id);