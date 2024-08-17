CREATE TABLE area
(
    area_code    VARCHAR(50) PRIMARY KEY,
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

CREATE TABLE content_display
(
    id                 VARCHAR(50) PRIMARY KEY,
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
    id          VARCHAR(50) PRIMARY KEY,
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
    id            VARCHAR(50) PRIMARY KEY,
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
    cust_type   VARCHAR(50) PRIMARY KEY,
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

CREATE TABLE group_news
(
    id            VARCHAR(50) PRIMARY KEY,
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
    id               VARCHAR(50) PRIMARY KEY,
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
    id            VARCHAR(50) PRIMARY KEY,
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
    id            VARCHAR(50) PRIMARY KEY,
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
    id            VARCHAR(50) PRIMARY KEY,
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
    id          VARCHAR(50) PRIMARY KEY,
    title       VARCHAR(500),
    content     TEXT,
    path        VARCHAR(255),
    news_type   VARCHAR(50),
    source_type VARCHAR(50),
    update_at   TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE news_content
(
    id           VARCHAR(50) PRIMARY KEY,
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
    id          VARCHAR(50) PRIMARY KEY,
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
    id            VARCHAR(50) PRIMARY KEY,
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
    id        VARCHAR(50) PRIMARY KEY,
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
    id           VARCHAR(50) PRIMARY KEY,
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
    id           VARCHAR(50) PRIMARY KEY,
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
    id          VARCHAR(50) PRIMARY KEY,
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
    id                VARCHAR(50) PRIMARY KEY,
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
    id                 VARCHAR(50) PRIMARY KEY,
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
    id        VARCHAR(50) PRIMARY KEY,
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
