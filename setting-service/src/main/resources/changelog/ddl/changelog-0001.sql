-- database changelog
-- Date: 2024-10-27

CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
-- UUID PRIMARY KEY DEFAULT gen_random_uuid()

CREATE TABLE area
(
    area_code    VARCHAR(36) PRIMARY KEY,
    parent_code  VARCHAR(50),
    province     VARCHAR(100),
    district     VARCHAR(100),
    precinct     VARCHAR(100),
    street_block VARCHAR(100),
    full_name    VARCHAR(255),
    name         VARCHAR(255),
    status       INTEGER,
    center       VARCHAR(100),
    create_by    VARCHAR(50),
    update_by    VARCHAR(50),
    create_at    TIMESTAMP,
    update_at    TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
INSERT INTO area (area_code, parent_code, province, district, precinct, street_block, full_name, name, status, center, create_by, update_by, create_at)
VALUES
    ('(0)24', NULL, 'Hà Nội', 'Quận Hoàn Kiếm', 'Phường Hàng Bạc', NULL, 'Hà Nội, Quận Hoàn Kiếm, Phường Hàng Bạc', 'Hoàn Kiếm', 1, 'Trung tâm Hà Nội', 'admin', 'admin', CURRENT_TIMESTAMP),
    ('(0)28', NULL, 'Hồ Chí Minh', 'Quận 1', 'Phường Bến Nghé', NULL, 'Hồ Chí Minh, Quận 1, Phường Bến Nghé', 'Quận 1', 1, 'Trung tâm Hồ Chí Minh', 'admin', 'admin', CURRENT_TIMESTAMP),
    ('(0)511', NULL, 'Đà Nẵng', 'Quận Hải Châu', 'Phường Thạch Thang', NULL, 'Đà Nẵng, Quận Hải Châu, Phường Thạch Thang', 'Hải Châu', 1, 'Trung tâm Đà Nẵng', 'admin', 'admin', CURRENT_TIMESTAMP),
    ('(0)225', NULL, 'Hải Phòng', 'Quận Hồng Bàng', 'Phường Hạ Lý', NULL, 'Hải Phòng, Quận Hồng Bàng, Phường Hạ Lý', 'Hồng Bàng', 1, 'Trung tâm Hải Phòng', 'admin', 'admin', CURRENT_TIMESTAMP),
    ('(0)238', NULL, 'Nghệ An', 'Thành phố Vinh', 'Phường Quang Trung', NULL, 'Nghệ An, Thành phố Vinh, Phường Quang Trung', 'Vinh', 1, 'Trung tâm Nghệ An', 'admin', 'admin', CURRENT_TIMESTAMP),
    ('(0)234', NULL, 'Thừa Thiên Huế', 'Thành phố Huế', 'Phường Phú Hòa', NULL, 'Thừa Thiên Huế, Thành phố Huế, Phường Phú Hòa', 'Huế', 1, 'Trung tâm Thừa Thiên Huế', 'admin', 'admin', CURRENT_TIMESTAMP),
    ('(0)235', NULL, 'Quảng Nam', 'Thành phố Tam Kỳ', 'Phường Tân Thạnh', NULL, 'Quảng Nam, Thành phố Tam Kỳ, Phường Tân Thạnh', 'Tam Kỳ', 1, 'Trung tâm Quảng Nam', 'admin', 'admin', CURRENT_TIMESTAMP),
    ('(0)251', NULL, 'Đồng Nai', 'Thành phố Biên Hòa', 'Phường Bửu Long', NULL, 'Đồng Nai, Thành phố Biên Hòa, Phường Bửu Long', 'Biên Hòa', 1, 'Trung tâm Đồng Nai', 'admin', 'admin', CURRENT_TIMESTAMP),
    ('(0)274', NULL, 'Bình Dương', 'Thành phố Thủ Dầu Một', 'Phường Phú Hòa', NULL, 'Bình Dương, Thành phố Thủ Dầu Một, Phường Phú Hòa', 'Thủ Dầu Một', 1, 'Trung tâm Bình Dương', 'admin', 'admin', CURRENT_TIMESTAMP),
    ('(0)239', NULL, 'Hà Tĩnh', 'Thành phố Hà Tĩnh', 'Phường Nam Hà', NULL, 'Hà Tĩnh, Thành phố Hà Tĩnh, Phường Nam Hà', 'Hà Tĩnh', 1, 'Trung tâm Hà Tĩnh', 'admin', 'admin', CURRENT_TIMESTAMP);


CREATE TABLE content_display
(
    id                 VARCHAR(36) PRIMARY KEY,
    page_id            VARCHAR(50),
    type               VARCHAR(50),
    content            TEXT,
    telecom_service_id VARCHAR(50),
    service_alias      VARCHAR(100),
    title              VARCHAR(255),
    subtitle           VARCHAR(255),
    icon               VARCHAR(255),
    image              VARCHAR(255),
    background_image   VARCHAR(255),
    display_order      INTEGER,
    ref_url            VARCHAR(255),
    parent_id          VARCHAR(50),
    status             INTEGER,
    create_by          VARCHAR(50),
    update_by          VARCHAR(50),
    create_at          TIMESTAMP,
    update_at          TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    is_original        BOOLEAN,
    name               VARCHAR(255),
    description        TEXT,
    screen_url         VARCHAR(255)
);

CREATE TABLE content_news
(
    id          VARCHAR(36) PRIMARY KEY,
    source_type VARCHAR(50),
    news_type   INTEGER,
    image       VARCHAR(255),
    title       VARCHAR(255),
    content     TEXT,
    path        VARCHAR(255),
    status      INTEGER,
    create_by   VARCHAR(50),
    update_by   VARCHAR(50),
    create_at   TIMESTAMP,
    update_at   TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE content_section
(
    id            VARCHAR(36) PRIMARY KEY,
    parent_id     VARCHAR(50),
    section_id    VARCHAR(50),
    type          VARCHAR(50),
    ref_id        VARCHAR(50),
    ref_alias     VARCHAR(100),
    ref_type      VARCHAR(50),
    name          VARCHAR(255),
    display_order BIGINT,
    path          VARCHAR(255),
    status        INTEGER,
    create_by     VARCHAR(50),
    create_at     TIMESTAMP,
    update_by     VARCHAR(50),
    update_at     TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE cust_type
(
    cust_type   VARCHAR(36) PRIMARY KEY,
    name        VARCHAR(255),
    group_type  VARCHAR(50),
    tax         INTEGER,
    plan        VARCHAR(50),
    status      INTEGER,
    description TEXT,
    create_by   VARCHAR(50),
    create_at   TIMESTAMP,
    update_by   VARCHAR(50),
    update_at   TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
INSERT INTO cust_type (cust_type, name, group_type, tax, plan, status, description, create_by, create_at, update_by)
VALUES
    ('CUST_PER', 'Khách hàng cá nhân', 'Cá nhân', 10, 'Gói cơ bản', 1, 'Khách hàng thuộc loại cá nhân.', 'admin', CURRENT_TIMESTAMP, 'admin'),
    ('CUST_BIZ', 'Khách hàng doanh nghiệp', 'Doanh nghiệp', 20, 'Gói nâng cao', 1, 'Khách hàng thuộc loại doanh nghiệp.', 'admin', CURRENT_TIMESTAMP, 'admin'),
    ('CUST_VIP', 'Khách hàng VIP', 'VIP', 5, 'Gói đặc biệt', 1, 'Khách hàng đặc biệt với ưu đãi riêng.', 'admin', CURRENT_TIMESTAMP, 'admin'),
    ('CUST_NEW', 'Khách hàng mới', 'Cá nhân', 10, 'Gói khuyến mãi', 1, 'Khách hàng mới tham gia dịch vụ.', 'admin', CURRENT_TIMESTAMP, 'admin'),
    ('CUST_LOYAL', 'Khách hàng trung thành', 'Cá nhân', 5, 'Gói ưu đãi', 1, 'Khách hàng đã sử dụng dịch vụ lâu dài.', 'admin', CURRENT_TIMESTAMP, 'admin');

CREATE TABLE group_news
(
    id            VARCHAR(36) PRIMARY KEY,
    name          VARCHAR(255),
    code          VARCHAR(50),
    display_order INTEGER,
    status        INTEGER,
    create_by     VARCHAR(50),
    create_at     TIMESTAMP,
    update_by     VARCHAR(50),
    update_at     TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE market_info
(
    id               VARCHAR(36) PRIMARY KEY,
    service_id       VARCHAR(50),
    service_alias    VARCHAR(100),
    title            VARCHAR(255),
    navigator_url    VARCHAR(255),
    market_order     INTEGER,
    market_image_url VARCHAR(255),
    create_by        VARCHAR(50),
    update_by        VARCHAR(50),
    create_at        TIMESTAMP,
    update_at        TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    status           INTEGER
);

CREATE TABLE market_page
(
    id            VARCHAR(36) PRIMARY KEY,
    service_id    VARCHAR(50),
    service_alias VARCHAR(100),
    name_service  VARCHAR(255),
    code          VARCHAR(50),
    name          VARCHAR(255),
    description   TEXT,
    status        INTEGER,
    create_by     VARCHAR(50),
    create_at     TIMESTAMP,
    update_by     VARCHAR(50),
    update_at     TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE market_section
(
    id            VARCHAR(36) PRIMARY KEY,
    type          VARCHAR(50),
    code          VARCHAR(50),
    name          VARCHAR(255),
    description   TEXT,
    display_order BIGINT,
    data          TEXT,
    status        INTEGER,
    create_by     VARCHAR(50),
    create_at     TIMESTAMP,
    update_by     VARCHAR(50),
    update_at     TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE news_info
(
    id            VARCHAR(36) PRIMARY KEY,
    title         VARCHAR(255),
    code          VARCHAR(50),
    display_order INTEGER,
    group_news_id VARCHAR(50),
    summary       TEXT,
    navigator_url VARCHAR(255),
    state         VARCHAR(50),
    status        INTEGER,
    create_by     VARCHAR(50),
    create_at     TIMESTAMP,
    update_by     VARCHAR(50),
    update_at     TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE news
(
    id          VARCHAR(36) PRIMARY KEY,
    title       VARCHAR(500),
    content     TEXT,
    path        VARCHAR(255),
    news_type   VARCHAR(50),
    source_type VARCHAR(50),
    update_at   TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE news_content
(
    id           VARCHAR(36) PRIMARY KEY,
    content      TEXT,
    news_info_id VARCHAR(50),
    status       INTEGER,
    create_by    VARCHAR(50),
    create_at    TIMESTAMP,
    update_by    VARCHAR(50),
    update_at    TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE option_set
(
    id          VARCHAR(36) PRIMARY KEY,
    code        VARCHAR(50),
    description TEXT,
    status      INTEGER,
    create_by   VARCHAR(50),
    create_at   TIMESTAMP,
    update_by   VARCHAR(50),
    update_at   TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE option_set_value
(
    id            VARCHAR(36) PRIMARY KEY,
    option_set_id VARCHAR(50),
    code          VARCHAR(50),
    value         VARCHAR(255),
    description   TEXT,
    status        INTEGER,
    create_by     VARCHAR(50),
    create_at     TIMESTAMP,
    update_by     VARCHAR(50),
    update_at     TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE page
(
    id        VARCHAR(36) PRIMARY KEY,
    code      VARCHAR(50),
    status    INTEGER,
    title     VARCHAR(255),
    logo_url  VARCHAR(255),
    create_by VARCHAR(50),
    update_by VARCHAR(50),
    create_at TIMESTAMP,
    update_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE page_component
(
    id           VARCHAR(36) PRIMARY KEY,
    page_id      VARCHAR(50),
    component_id VARCHAR(50),
    status       INTEGER,
    create_by    VARCHAR(50),
    create_at    TIMESTAMP,
    update_by    VARCHAR(50),
    update_at    TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE service_media
(
    id           VARCHAR(36) PRIMARY KEY,
    service_id   VARCHAR(50),
    type         VARCHAR(50),
    url          VARCHAR(255),
    content_type VARCHAR(50),
    status       INTEGER,
    create_by    VARCHAR(50),
    create_at    TIMESTAMP,
    update_by    VARCHAR(50),
    update_at    TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE setting
(
    id          VARCHAR(36) PRIMARY KEY,
    code        VARCHAR(50),
    value       VARCHAR(255),
    description TEXT,
    status      INTEGER,
    create_by   VARCHAR(50),
    create_at   TIMESTAMP,
    update_by   VARCHAR(50),
    update_at   TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE telecom_service
(
    id                VARCHAR(36) PRIMARY KEY,
    name              VARCHAR(255),
    service_alias     VARCHAR(100),
    description       TEXT,
    image             VARCHAR(255),
    origin_id         VARCHAR(50),
    status            INTEGER,
    is_filter         BOOLEAN,
    group_id          VARCHAR(50),
    create_by         VARCHAR(50),
    create_at         TIMESTAMP,
    update_by         VARCHAR(50),
    update_at         TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    deploy_order_code VARCHAR(50),
    bccs_alias        VARCHAR(50)
);

CREATE TABLE telecom_service_config
(
    id                 VARCHAR(36) PRIMARY KEY,
    config             TEXT,
    telecom_service_id VARCHAR(50),
    status             INTEGER,
    create_by          VARCHAR(50),
    create_at          TIMESTAMP,
    update_by          VARCHAR(50),
    update_at          TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE upload_images
(
    id        VARCHAR(36) PRIMARY KEY,
    name      VARCHAR(255),
    type      INTEGER,
    path      VARCHAR(255),
    parent_id VARCHAR(50),
    status    INTEGER,
    create_at TIMESTAMP,
    create_by VARCHAR(50),
    update_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_by VARCHAR(50)
);
