-- database changelog
-- Date: 2025-02-16

-- Tao bang rating
CREATE TABLE rating
(
    id               VARCHAR(36)  NOT NULL PRIMARY KEY,
    rating_type_code VARCHAR(100) NULL,
    target_id        VARCHAR(100) NULL,
    username         VARCHAR(100) NULL,
    cust_name        VARCHAR(200) NULL,
    rating           FLOAT        NULL,
    content          TEXT         NULL,
    rating_date      TIMESTAMP    NULL,
    has_image        INT          NULL,
    has_video        INT          NULL,
    status           INT          NULL,
    state            VARCHAR(50)  NULL,
    display_status   INT          NULL,
    sum_rate_status  INT          NULL,
    target_user      VARCHAR(100) NULL,
    create_by        VARCHAR(200) NULL,
    create_at        TIMESTAMP    NULL,
    update_by        VARCHAR(200) NULL,
    update_at        TIMESTAMP    NULL,
    service_alias    VARCHAR(100) NULL
);
COMMENT ON TABLE rating IS 'Bang luu tru thong tin danh gia';
COMMENT ON COLUMN rating.rating_type_code IS 'Ma cua bang rating_type';
COMMENT ON COLUMN rating.target_id IS 'Ma cua doi tuong danh gia. Vi du rating_type_code = service thi target_id la id cua bang telecom_service';
COMMENT ON COLUMN rating.username IS 'Username dang nhap he thong danh gia';
COMMENT ON COLUMN rating.cust_name IS 'Ten khach hang danh gia';
COMMENT ON COLUMN rating.rating IS 'So diem danh gia';
COMMENT ON COLUMN rating.content IS 'Noi dung comment';
COMMENT ON COLUMN rating.rating_date IS 'Ngay comment';
COMMENT ON COLUMN rating.has_image IS 'Co anh hay khong';
COMMENT ON COLUMN rating.has_video IS 'Co video hay khong';
COMMENT ON COLUMN rating.status IS 'Trang thai 1 la hieu luc, 0 la khong hieu luc';
COMMENT ON COLUMN rating.state IS 'wait_approve: Cho phe duyet, approved: Phe duyet, wait_approve_fix: Cho phe duyet sua chua';
COMMENT ON COLUMN rating.display_status IS '1 la hien thi, 0 hoac null la khong hien thi';
COMMENT ON COLUMN rating.sum_rate_status IS '1 la tinh vao tong danh gia, 0 la khong tinh vao tong danh gia';
COMMENT ON COLUMN rating.target_user IS 'Doi tuong danh gia CUSTOMER, ADMIN';
COMMENT ON COLUMN rating.create_by IS 'Nguoi tao';
COMMENT ON COLUMN rating.create_at IS 'Ngay tao';
COMMENT ON COLUMN rating.update_by IS 'Nguoi cap nhat';
COMMENT ON COLUMN rating.update_at IS 'Ngay cap nhat';
COMMENT ON COLUMN rating.service_alias IS 'alias dich vu tren SME HUB';

-- Tao bang rating_count
CREATE TABLE rating_count
(
    id               VARCHAR(36)  NOT NULL PRIMARY KEY,
    rating_type_code VARCHAR(100) NULL,
    target_id        VARCHAR(100) NULL,
    number_rate      INT          NULL,
    rating           FLOAT        NULL,
    max_rating       FLOAT        NULL,
    detail           VARCHAR(200) NULL,
    service_alias    VARCHAR(100) NULL
);
COMMENT ON TABLE rating_count IS 'Bang luu tru tong hop so luot danh gia';
COMMENT ON COLUMN rating_count.rating_type_code IS 'Ma cua bang rating_type';
COMMENT ON COLUMN rating_count.target_id IS 'Ma cua doi tuong danh gia. Vi du rating_type_code = service thi target_id la id cua bang telecom_service';
COMMENT ON COLUMN rating_count.number_rate IS 'Tong so luot danh gia';
COMMENT ON COLUMN rating_count.rating IS 'So diem danh gia';
COMMENT ON COLUMN rating_count.max_rating IS 'Diem danh gia toi da';
COMMENT ON COLUMN rating_count.detail IS 'Chi tiet tung loai danh gia. json luu cac gia tri {"5":"12"} danh gia 5 sao la 12 danh gia';
COMMENT ON COLUMN rating_count.service_alias IS 'alias dich vu tren SME HUB';

-- Tao bang rating_history
CREATE TABLE rating_history
(
    id         VARCHAR(36)  NOT NULL PRIMARY KEY,
    rating_id  VARCHAR(36)  NULL,
    rating_bf  FLOAT        NULL,
    rating_af  FLOAT        NULL,
    content_bf TEXT         NULL,
    content_af TEXT         NULL,
    approve_at TIMESTAMP    NULL,
    approve_by VARCHAR(100) NULL,
    state      VARCHAR(50)  NULL,
    status     INT          NULL,
    create_by  VARCHAR(200) NULL,
    create_at  TIMESTAMP    NULL,
    update_by  VARCHAR(200) NULL,
    update_at  TIMESTAMP    NULL,
    CONSTRAINT fk_rating_history_rating FOREIGN KEY (rating_id) REFERENCES rating (id)
);
COMMENT ON TABLE rating_history IS 'Bang luu tru lich su thay doi danh gia';
COMMENT ON COLUMN rating_history.rating_id IS 'Ma cua bang rating';
COMMENT ON COLUMN rating_history.rating_bf IS 'So diem danh gia truoc sua';
COMMENT ON COLUMN rating_history.rating_af IS 'So diem danh gia sau sua';
COMMENT ON COLUMN rating_history.content_bf IS 'Noi dung comment truoc sua';
COMMENT ON COLUMN rating_history.content_af IS 'Noi dung comment sau sua';
COMMENT ON COLUMN rating_history.approve_at IS 'Ngay phe duyet';
COMMENT ON COLUMN rating_history.approve_by IS 'Nguoi phe duyet';
COMMENT ON COLUMN rating_history.state IS 'wait_approve: Cho phe duyet, approved: Phe duyet, wait_approve_fix: Cho phe duyet sua chua';
COMMENT ON COLUMN rating_history.status IS 'Trang thai 1 la hieu luc, 0 la khong hieu luc';
COMMENT ON COLUMN rating_history.create_by IS 'Nguoi tao';
COMMENT ON COLUMN rating_history.create_at IS 'Ngay tao';
COMMENT ON COLUMN rating_history.update_by IS 'Nguoi cap nhat';
COMMENT ON COLUMN rating_history.update_at IS 'Ngay cap nhat';

-- Tao bang rating_media
CREATE TABLE rating_media
(
    id          VARCHAR(36)   NOT NULL PRIMARY KEY,
    rating_id   VARCHAR(36)   NULL,
    response_id VARCHAR(36)   NULL,
    state       VARCHAR(50)   NULL,
    status      INT           NULL,
    media_type  VARCHAR(100)  NULL,
    file_path   VARCHAR(2000) NULL,
    create_by   VARCHAR(200)  NULL,
    create_at   DATE          NULL,
    update_by   VARCHAR(200)  NULL,
    update_at   DATE          NULL,
    CONSTRAINT fk_rating_media_rating FOREIGN KEY (rating_id) REFERENCES rating (id)
);
COMMENT ON TABLE rating_media IS 'Bang luu tru thong tin media cua danh gia';
COMMENT ON COLUMN rating_media.rating_id IS 'Ma cua bang rating';
COMMENT ON COLUMN rating_media.response_id IS 'Ma cua bang rating_response';
COMMENT ON COLUMN rating_media.state IS 'wait_approve: Cho phe duyet, approved: Phe duyet, wait_approve_fix: Cho phe duyet sua chua';
COMMENT ON COLUMN rating_media.status IS 'Trang thai 1 la hieu luc, 0 la khong hieu luc';
COMMENT ON COLUMN rating_media.media_type IS 'Loai media IMAGE, VIDEO';
COMMENT ON COLUMN rating_media.file_path IS 'Duong dan luu thong tin file da phuong tien';
COMMENT ON COLUMN rating_media.create_by IS 'Nguoi tao';
COMMENT ON COLUMN rating_media.create_at IS 'Ngay tao';
COMMENT ON COLUMN rating_media.update_by IS 'Nguoi cap nhat';
COMMENT ON COLUMN rating_media.update_at IS 'Ngay cap nhat';

-- Tao bang rating_response
CREATE TABLE rating_response
(
    id             VARCHAR(36)  NOT NULL PRIMARY KEY,
    rating_id      VARCHAR(36)  NULL,
    username       VARCHAR(100) NULL,
    content        TEXT         NULL,
    response_date  DATE         NULL,
    state          VARCHAR(50)  NULL,
    status         INT          NULL,
    display_status INT          NULL,
    create_by      VARCHAR(200) NULL,
    create_at      DATE         NULL,
    update_by      VARCHAR(200) NULL,
    update_at      DATE         NULL,
    CONSTRAINT fk_rating_response_rating FOREIGN KEY (rating_id) REFERENCES rating (id)
);
COMMENT ON TABLE rating_response IS 'Bang luu tru phan hoi danh gia';
COMMENT ON COLUMN rating_response.rating_id IS 'Ma cua bang rating';
COMMENT ON COLUMN rating_response.username IS 'Username dang nhap he thong danh gia';
COMMENT ON COLUMN rating_response.content IS 'Noi dung comment';
COMMENT ON COLUMN rating_response.response_date IS 'Ngay comment';
COMMENT ON COLUMN rating_response.state IS 'wait_approve: Cho phe duyet, approved: Phe duyet, wait_approve_fix: Cho phe duyet sua chua';
COMMENT ON COLUMN rating_response.status IS 'Trang thai 1 la hieu luc, 0 la khong hieu luc';
COMMENT ON COLUMN rating_response.display_status IS '1 la hien thi, 0 hoac null la khong hien thi';
COMMENT ON COLUMN rating_response.create_by IS 'Nguoi tao';
COMMENT ON COLUMN rating_response.create_at IS 'Ngay tao';
COMMENT ON COLUMN rating_response.update_by IS 'Nguoi cap nhat';
COMMENT ON COLUMN rating_response.update_at IS 'Ngay cap nhat';

-- Tao bang rating_type
CREATE TABLE rating_type
(
    id          VARCHAR(36)   NOT NULL PRIMARY KEY,
    code        VARCHAR(100)  NULL,
    name        VARCHAR(200)  NULL,
    description VARCHAR(1000) NULL,
    status      INT           NULL,
    create_by   VARCHAR(200)  NULL,
    create_at   DATE          NULL,
    update_by   VARCHAR(200)  NULL,
    update_at   DATE          NULL
);
COMMENT ON TABLE rating_type IS 'Bang luu tru loai danh gia';
COMMENT ON COLUMN rating_type.code IS 'Ma loai danh gia service/product/order';
COMMENT ON COLUMN rating_type.name IS 'Ten loai danh gia';
COMMENT ON COLUMN rating_type.description IS 'Mo ta loai danh gia';
COMMENT ON COLUMN rating_type.status IS 'Trang thai loai danh gia';
COMMENT ON COLUMN rating_type.create_by IS 'Nguoi tao';
COMMENT ON COLUMN rating_type.create_at IS 'Ngay tao';
COMMENT ON COLUMN rating_type.update_by IS 'Nguoi cap nhat';
COMMENT ON COLUMN rating_type.update_at IS 'Ngay cap nhat';

-- Tao bang service_rating
CREATE TABLE service_rating
(
    create_at          TIMESTAMP    NULL,
    tag                TEXT         NULL,
    create_by          VARCHAR(50)  NULL,
    target             VARCHAR(200) NULL,
    sum_rate_status    INT          NULL,
    display_status     INT          NULL,
    state              VARCHAR(20)  NULL,
    rating_status      INT          NULL,
    has_video          INT          NULL,
    has_image          INT          NULL,
    fix_tag            TEXT         NULL,
    comment_date       TIMESTAMP    NULL,
    fix_content        TEXT         NULL,
    content            TEXT         NULL,
    fix_rating         FLOAT        NULL,
    rating             FLOAT        NULL,
    cust_name          VARCHAR(200) NULL,
    username           VARCHAR(50)  NULL,
    order_id           VARCHAR(20)  NULL,
    product_id         VARCHAR(20)  NULL,
    telecom_service_id VARCHAR(36)  NULL,
    rating_id          VARCHAR(36)  NULL,
    id                 VARCHAR(36)  NOT NULL PRIMARY KEY,
    update_by          VARCHAR(50)  NULL,
    update_at          TIMESTAMP    NULL,
    CONSTRAINT fk_service_rating_rating FOREIGN KEY (rating_id) REFERENCES rating (id)
);
COMMENT ON TABLE service_rating IS 'Bang luu tru danh gia dich vu';
COMMENT ON COLUMN service_rating.create_at IS 'Ngay tao';
COMMENT ON COLUMN service_rating.tag IS 'Tag';
COMMENT ON COLUMN service_rating.create_by IS 'Nguoi tao';
COMMENT ON COLUMN service_rating.target IS 'Doi tuong danh gia';
COMMENT ON COLUMN service_rating.sum_rate_status IS '1 la tinh vao tong danh gia, 0 la khong tinh vao tong danh gia';
COMMENT ON COLUMN service_rating.display_status IS '1 la hien thi, 0 hoac null la khong hien thi';
COMMENT ON COLUMN service_rating.state IS 'Trang thai';
COMMENT ON COLUMN service_rating.rating_status IS 'Trang thai danh gia';
COMMENT ON COLUMN service_rating.has_video IS 'Co video hay khong';
COMMENT ON COLUMN service_rating.has_image IS 'Co anh hay khong';
COMMENT ON COLUMN service_rating.fix_tag IS 'Tag sua chua';
COMMENT ON COLUMN service_rating.comment_date IS 'Ngay comment';
COMMENT ON COLUMN service_rating.fix_content IS 'Noi dung sua chua';
COMMENT ON COLUMN service_rating.content IS 'Noi dung comment';
COMMENT ON COLUMN service_rating.fix_rating IS 'Diem danh gia sua chua';
COMMENT ON COLUMN service_rating.rating IS 'Diem danh gia';
COMMENT ON COLUMN service_rating.cust_name IS 'Ten khach hang danh gia';
COMMENT ON COLUMN service_rating.username IS 'Username dang nhap he thong danh gia';
COMMENT ON COLUMN service_rating.order_id IS 'Ma don hang';
COMMENT ON COLUMN service_rating.product_id IS 'Ma san pham';
COMMENT ON COLUMN service_rating.telecom_service_id IS 'Ma dich vu telecom';
COMMENT ON COLUMN service_rating.rating_id IS 'Ma cua bang rating';
COMMENT ON COLUMN service_rating.update_by IS 'Nguoi cap nhat';
COMMENT ON COLUMN service_rating.update_at IS 'Ngay cap nhat';