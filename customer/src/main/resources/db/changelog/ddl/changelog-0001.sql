--changeset hoangtien2k3:issue-1
CREATE TABLE shop_user
(
    id                SERIAL PRIMARY KEY,
    customer_id       INT UNIQUE  NOT NULL,
    status_view       INT         NOT NULL DEFAULT 0,
    status_account    INT         NOT NULL DEFAULT 0,
    last_login        TIMESTAMP,
    expires_at        TIMESTAMP,
    roles             VARCHAR(50) NOT NULL,
    security_question VARCHAR(255),
    security_answer   VARCHAR(255),
    created_at        TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at        TIMESTAMP,
    loyalty_points    INT         NOT NULL DEFAULT 0
);

CREATE TABLE user_oauth
(
    id            SERIAL PRIMARY KEY,
    shop_user_id  INT UNIQUE,
    identifier    VARCHAR(255) NOT NULL,
    access_token  TEXT,
    refresh_token TEXT
);
CREATE INDEX user_oauth_user_id_index ON user_oauth (shop_user_id);

CREATE TABLE address
(
    id             SERIAL PRIMARY KEY,
    customer_id    INT,
    street         VARCHAR(100),
    province       VARCHAR(100),
    district       VARCHAR(100),
    precinct       VARCHAR(100),
    city           VARCHAR(100),
    address_detail VARCHAR(50),
    postcode       VARCHAR(255),
    country_code   VARCHAR(50),
    created_at     TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at     TIMESTAMP,
    company        VARCHAR(255)
);
CREATE INDEX address_customer_id_index ON address (customer_id);

-- Table "customer"
CREATE TABLE customer
(
    id                       SERIAL PRIMARY KEY,
    customer_group_id        INT,
    email                    VARCHAR(100) UNIQUE NOT NULL,
    username                 VARCHAR(255),
    password                 VARCHAR(255)        NOT NULL,
    first_name               VARCHAR(255),
    last_name                VARCHAR(255),
    display_name             VARCHAR(200),
    birthday                 TIMESTAMP,
    image_url                VARCHAR(255),
    phone_number             VARCHAR(20)         NOT NULL,
    gender                   VARCHAR(10)         NOT NULL DEFAULT 'OTHER',
    created_at               TIMESTAMP           NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at               TIMESTAMP,
    email_verified           INT                 NOT NULL DEFAULT 0,
    subscribed_to_newsletter BOOLEAN             NOT NULL DEFAULT FALSE
);
CREATE INDEX customer_customer_group_id_index ON customer (customer_group_id);
CREATE INDEX customer_created_at_index ON customer (created_at);

CREATE TABLE customer_group
(
    id   SERIAL PRIMARY KEY,
    code VARCHAR(255) UNIQUE NOT NULL,
    name VARCHAR(255)        NOT NULL
);

ALTER TABLE user_oauth
    ADD CONSTRAINT user_oauth_user_id_foreign FOREIGN KEY (shop_user_id) REFERENCES shop_user (id);
ALTER TABLE shop_user
    ADD CONSTRAINT shop_user_customer_id_foreign FOREIGN KEY (customer_id) REFERENCES customer (id);
ALTER TABLE address
    ADD CONSTRAINT address_customer_id_foreign FOREIGN KEY (customer_id) REFERENCES customer (id);
ALTER TABLE customer
    ADD CONSTRAINT customer_customer_group_id_foreign FOREIGN KEY (customer_group_id) REFERENCES customer_group (id);
