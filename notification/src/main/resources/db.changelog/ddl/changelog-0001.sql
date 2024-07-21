-- changeset liquibase:0001
-- author: hoangtien2k3
-- create table channel, notification_category, notification_content, notification, transmission
-- database: notification

CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE channel
(
    id     UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    status INTEGER,
    type   VARCHAR(255)
);

CREATE TABLE notification_category
(
    id     UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    status INTEGER,
    type   VARCHAR(255)
);

CREATE TABLE notification_content
(
    id            UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    title         VARCHAR(255),
    sub_title     VARCHAR(255),
    image_url     VARCHAR(255),
    url           VARCHAR(255),
    status        INTEGER,
    template_mail TEXT,
    external_data TEXT,
    create_at     TIMESTAMP,
    create_by     VARCHAR(255),
    update_at     TIMESTAMP,
    update_by     VARCHAR(255)
);

CREATE TABLE notification
(
    id                      UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    sender                  VARCHAR(255),
    severity                VARCHAR(255),
    notification_content_id UUID,
    expect_send_time        TIMESTAMP,
    status                  INTEGER,
    content_type            VARCHAR(255),
    category_id             UUID,
    create_at               TIMESTAMP,
    create_by               VARCHAR(255),
    update_at               TIMESTAMP,
    update_by               VARCHAR(255)
);

CREATE TABLE transmission
(
    id              UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    notification_id UUID,
    receiver        VARCHAR(255),
    email           VARCHAR(255),
    channel_id      UUID,
    state           VARCHAR(255),
    status          INTEGER,
    resend_count    INTEGER,
    create_at       TIMESTAMP,
    create_by       VARCHAR(255),
    update_at       TIMESTAMP,
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
