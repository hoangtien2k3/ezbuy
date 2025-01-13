-- changeset liquibase:0001
-- author: hoangtien2k3
-- create table channel, notification_category, notification_content, notification, transmission

-- table channel
CREATE TABLE channel
(
    id     VARCHAR(36) PRIMARY KEY NOT NULL,
    status SMALLINT default 1      NULL,
    type   VARCHAR(100)            NOT NULL
);
COMMENT ON TABLE channel IS 'Bang luu thong tin cac kenh (channel)';
COMMENT ON COLUMN channel.id IS 'Id ban ghi kenh gui thong bao';
COMMENT ON COLUMN channel.status IS 'Co hieu luc khong';
COMMENT ON COLUMN channel.type IS 'Type kenh gui thong bao';

-- table notification_category
CREATE TABLE notification_category
(
    id     VARCHAR(36) PRIMARY KEY NOT NULL,
    status SMALLINT DEFAULT 1      NULL,
    type   VARCHAR(100)
);
COMMENT ON TABLE notification_category IS 'Bang luu thong tin danh muc (notification_category)';
COMMENT ON COLUMN notification_category.id IS 'Dinh danh cua ban ghi';
COMMENT ON COLUMN notification_category.status IS 'Co hieu luc hay khong';
COMMENT ON COLUMN notification_category.type IS 'Luu gia tri loai thong bao';

-- table notification_content
CREATE TABLE notification_content
(
    id            VARCHAR(36) PRIMARY KEY               NOT NULL,
    title         VARCHAR(500)                          NULL,
    sub_title     VARCHAR(5000)                         NULL,
    image_url     VARCHAR(500)                          NULL,
    url           VARCHAR(300)                          NULL,
    status        SMALLINT  DEFAULT 1                   NULL,
    template_mail VARCHAR(50)                           NULL,
    external_data VARCHAR(255)                          NULL,
    create_at     TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    create_by     VARCHAR(100),
    update_at     TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    update_by     VARCHAR(100)
);
COMMENT ON TABLE notification_content IS 'Bang luu thong tin noi dung (notification_content)';
COMMENT ON COLUMN notification_content.id IS 'Ma dinh danh ban ghi';
COMMENT ON COLUMN notification_content.title IS 'Tieu de ban ghi';
COMMENT ON COLUMN notification_content.image_url IS 'Duong dan anh(neu co)';
COMMENT ON COLUMN notification_content.create_at IS 'Thoi diem khoi tao';
COMMENT ON COLUMN notification_content.create_by IS 'Doi tuong khoi tao';
COMMENT ON COLUMN notification_content.update_by IS 'Doi tuong cap nhat';
COMMENT ON COLUMN notification_content.url IS 'Duong dan thong bao';
COMMENT ON COLUMN notification_content.status IS 'Co hieu luc hay khong';

-- table notification
CREATE TABLE notification
(
    id                      VARCHAR(36) PRIMARY KEY             NOT NULL,
    sender                  VARCHAR(500)                        NULL,
    severity                VARCHAR(50)                         NULL,
    notification_content_id VARCHAR(36)                         NOT NULL,
    content_type            VARCHAR(100)                        NULL,
    create_at               TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    create_by               VARCHAR(100)                        NULL,
    update_at               TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    update_by               VARCHAR(100)                        NULL,
    category_id             VARCHAR(36)                         NOT NULL,
    status                  SMALLINT  DEFAULT 1                 NULL,
    expect_send_time        TIMESTAMP                           NULL
);
COMMENT ON TABLE notification IS 'Bang luu tru cac thong bao trong he thong';
COMMENT ON COLUMN notification.id IS 'Khoa chinh, dinh danh duy nhat cho moi thong bao';
COMMENT ON COLUMN notification.sender IS 'He thong tao thong bao';
COMMENT ON COLUMN notification.severity IS 'Muc do can thiet cua thong bao';
COMMENT ON COLUMN notification.notification_content_id IS 'Khoa ngoai tham chieu den bang notification_content';
COMMENT ON COLUMN notification.content_type IS 'Loai van ban gui di (text/plain hoac html/plain)';
COMMENT ON COLUMN notification.create_at IS 'Thoi gian khoi tao thong bao';
COMMENT ON COLUMN notification.create_by IS 'Doi tuong khoi tao thong bao';
COMMENT ON COLUMN notification.update_at IS 'Thoi gian cap nhat thong bao';
COMMENT ON COLUMN notification.update_by IS 'Doi tuong cap nhat thong bao';
COMMENT ON COLUMN notification.category_id IS 'Khoa ngoai tham chieu den bang notification_category';
COMMENT ON COLUMN notification.status IS 'Trang thai thong bao, 1: co hieu luc, 0: khong co hieu luc';
COMMENT ON COLUMN notification.expect_send_time IS 'Thoi gian mong muon gui thong bao';
-- Trigger de cap nhat update_at khi co thay doi bang notification
CREATE OR REPLACE FUNCTION update_timestamp_notification()
    RETURNS TRIGGER AS
$$
BEGIN
    NEW.update_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;
CREATE TRIGGER trigger_update_timestamp_notification
    BEFORE UPDATE
    ON notification
    FOR EACH ROW
EXECUTE FUNCTION update_timestamp_notification();

-- table transmission
CREATE TABLE transmission
(
    id              VARCHAR(36) PRIMARY KEY             NOT NULL,
    receiver        VARCHAR(100)                        NULL,
    state           VARCHAR(50)                         NULL,
    status          SMALLINT  DEFAULT 1                 NULL,
    resend_count    INT                                 NULL,
    create_at       TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    create_by       VARCHAR(100)                        NULL,
    update_at       TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    update_by       VARCHAR(100)                        NULL,
    channel_id      VARCHAR(36)                         NOT NULL,
    notification_id VARCHAR(36)                         NOT NULL,
    email           VARCHAR(200)                        NULL
);
COMMENT ON TABLE transmission IS 'Bang luu thong tin cac lan gui thong bao';
COMMENT ON COLUMN transmission.id IS 'Ma dinh danh ban ghi';
COMMENT ON COLUMN transmission.receiver IS 'ID cua nguoi nhan';
COMMENT ON COLUMN transmission.state IS 'Trang thai thong bao (PENDING, FAILED, UNREAD, READ, NEW)';
COMMENT ON COLUMN transmission.status IS 'Trang thai co hieu luc hay khong, 1: co hieu luc, 0: khong hieu luc';
COMMENT ON COLUMN transmission.resend_count IS 'So lan gui thong bao khong thanh cong';
COMMENT ON COLUMN transmission.create_at IS 'Thoi gian khoi tao';
COMMENT ON COLUMN transmission.create_by IS 'Doi tuong khoi tao';
COMMENT ON COLUMN transmission.update_at IS 'Thoi gian cap nhat';
COMMENT ON COLUMN transmission.update_by IS 'Doi tuong cap nhat';
COMMENT ON COLUMN transmission.channel_id IS 'Khoa ngoai den bang channel';
COMMENT ON COLUMN transmission.notification_id IS 'Khoa ngoai den bang notification';
COMMENT ON COLUMN transmission.email IS 'Email cua nguoi nhan';
-- Trigger de cap nhat update_at khi co thay doi bang transmission
CREATE OR REPLACE FUNCTION update_timestamp_transmission()
    RETURNS TRIGGER AS
$$
BEGIN
    NEW.update_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;
CREATE TRIGGER trigger_update_timestamp_transmission
    BEFORE UPDATE
    ON transmission
    FOR EACH ROW
EXECUTE FUNCTION update_timestamp_transmission();

ALTER TABLE notification
    ADD CONSTRAINT notification_notification_content_id_foreign FOREIGN KEY (notification_content_id) REFERENCES notification_content (id);
ALTER TABLE notification
    ADD CONSTRAINT notification_category_id_foreign FOREIGN KEY (category_id) REFERENCES notification_category (id);
ALTER TABLE transmission
    ADD CONSTRAINT transmission_notification_id_foreign FOREIGN KEY (notification_id) REFERENCES notification (id);
ALTER TABLE transmission
    ADD CONSTRAINT transmission_channel_id_foreign FOREIGN KEY (channel_id) REFERENCES channel (id);
