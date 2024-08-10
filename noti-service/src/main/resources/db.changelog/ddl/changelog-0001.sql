-- changeset liquibase:0001
-- author: hoangtien2k3
-- create table channel, notification_category, notification_content, notification, transmission
-- database: notification

CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE channel
(
    id     VARCHAR(36) PRIMARY KEY,
    status INTEGER,
    type   VARCHAR(255)
);

CREATE TABLE notification_category
(
    id     VARCHAR(36) PRIMARY KEY,
    status INTEGER,
    type   VARCHAR(255)
);

CREATE TABLE notification_content
(
    id            VARCHAR(36) PRIMARY KEY,
    title         VARCHAR(255),
    sub_title     VARCHAR(255),
    image_url     VARCHAR(255),
    url           VARCHAR(255),
    status        INTEGER,
    template_mail TEXT,
    external_data TEXT,
    create_at     TIMESTAMP,
    create_by     VARCHAR(255),
    update_at     TIMESTAMP DEFAULT now(),
    update_by     VARCHAR(255)
);

CREATE TABLE notification
(
    id                      VARCHAR(36) PRIMARY KEY,
    sender                  VARCHAR(255),
    severity                VARCHAR(255),
    notification_content_id VARCHAR(36),
    expect_send_time        TIMESTAMP,
    status                  INTEGER,
    content_type            VARCHAR(255),
    category_id             VARCHAR(36),
    create_at               TIMESTAMP,
    create_by               VARCHAR(255),
    update_at               TIMESTAMP DEFAULT now(),
    update_by               VARCHAR(255)
);

CREATE TABLE transmission
(
    id              VARCHAR(36) PRIMARY KEY,
    notification_id VARCHAR(36),
    receiver        VARCHAR(255),
    email           VARCHAR(255),
    channel_id      VARCHAR(36),
    state           VARCHAR(255),
    status          INTEGER,
    resend_count    INTEGER,
    create_at       TIMESTAMP,
    create_by       VARCHAR(255),
    update_at       TIMESTAMP DEFAULT now(),
    update_by       VARCHAR(255)
);

ALTER TABLE notification
    ADD CONSTRAINT notification_notification_content_id_foreign FOREIGN KEY (notification_content_id) REFERENCES notification_content (id);
ALTER TABLE notification
    ADD CONSTRAINT notification_category_id_foreign FOREIGN KEY (category_id) REFERENCES notification_category (id);
ALTER TABLE transmission
    ADD CONSTRAINT transmission_notification_id_foreign FOREIGN KEY (notification_id) REFERENCES notification (id);
ALTER TABLE transmission
    ADD CONSTRAINT transmission_channel_id_foreign FOREIGN KEY (channel_id) REFERENCES channel (id);
