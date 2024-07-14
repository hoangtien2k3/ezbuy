CREATE TABLE channel
(
    id     SERIAL PRIMARY KEY,
    status INTEGER,
    type   VARCHAR(255)
);

CREATE TABLE notification_category
(
    id     SERIAL PRIMARY KEY,
    status INTEGER,
    type   VARCHAR(255)
);

CREATE TABLE notification_content
(
    id            SERIAL PRIMARY KEY,
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
    id                      SERIAL PRIMARY KEY,
    sender                  VARCHAR(255),
    severity                VARCHAR(255),
    notification_content_id INTEGER,
    expect_send_time        TIMESTAMP,
    status                  INTEGER,
    content_type            VARCHAR(255),
    category_id             INTEGER,
    create_at               TIMESTAMP,
    create_by               VARCHAR(255),
    update_at               TIMESTAMP,
    update_by               VARCHAR(255)
);

CREATE TABLE transmission
(
    id              SERIAL PRIMARY KEY,
    notification_id INTEGER,
    receiver        VARCHAR(255),
    email           VARCHAR(255),
    channel_id      INTEGER,
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
