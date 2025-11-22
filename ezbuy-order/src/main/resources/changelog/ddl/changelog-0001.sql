CREATE TABLE data_cust_service
(
    id            VARCHAR(50)                           NOT NULL,
    service_id    VARCHAR(50)                           NOT NULL,
    service_alias VARCHAR(100)                          NULL,
    data          VARCHAR(1000)                         NULL,
    status        SMALLINT                              NULL,
    create_at     TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP NOT NULL,
    create_by     VARCHAR(100)                          NULL,
    update_at     TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP NULL,
    update_by     VARCHAR(100)                          NULL,
    PRIMARY KEY (id, create_at)
);
CREATE INDEX idx_service_alias ON data_cust_service (service_alias);
CREATE INDEX idx_service_id ON data_cust_service (service_id);
CREATE INDEX idx_status ON data_cust_service (status);

CREATE TABLE invoice_info
(
    id                VARCHAR(36)                           NOT NULL PRIMARY KEY,
    user_id           VARCHAR(36)                           NULL,
    organization_id   VARCHAR(36)                           NULL,
    tax_code          VARCHAR(30)                           NULL,
    full_name         VARCHAR(255)                          NULL,
    phone             VARCHAR(12)                           NULL,
    email             VARCHAR(200)                          NULL,
    province_code     VARCHAR(5)                            NULL,
    province_name     VARCHAR(100)                          NULL,
    district_code     VARCHAR(5)                            NULL,
    district_name     VARCHAR(100)                          NULL,
    precinct_code     VARCHAR(5)                            NULL,
    precinct_name     VARCHAR(100)                          NULL,
    address_detail    VARCHAR(200)                          NULL,
    status            SMALLINT                              NULL,
    create_at         TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP NULL,
    create_by         VARCHAR(255)                          NULL,
    update_at         TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP NULL,
    update_by         VARCHAR(255)                          NULL,
    organization_name VARCHAR(255)                          NULL,
    pay_type          VARCHAR(150)                          NULL,
    account_number    VARCHAR(150)                          NULL
);
CREATE INDEX i_ii_oi ON invoice_info (organization_id);
CREATE INDEX i_ii_ui ON invoice_info (user_id);

CREATE TABLE invoice_info_history
(
    id                VARCHAR(36)                           NOT NULL PRIMARY KEY,
    user_id           VARCHAR(36)                           NULL,
    organization_id   VARCHAR(36)                           NULL,
    tax_code          VARCHAR(30)                           NULL,
    full_name         VARCHAR(255)                          NULL,
    phone             VARCHAR(12)                           NULL,
    email             VARCHAR(200)                          NULL,
    province_code     VARCHAR(5)                            NULL,
    province_name     VARCHAR(100)                          NULL,
    district_code     VARCHAR(5)                            NULL,
    district_name     VARCHAR(100)                          NULL,
    precinct_code     VARCHAR(5)                            NULL,
    precinct_name     VARCHAR(100)                          NULL,
    address_detail    VARCHAR(200)                          NULL,
    status            SMALLINT                              NULL,
    create_at         TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP NULL,
    create_by         VARCHAR(255)                          NULL,
    organization_name VARCHAR(255)                          NULL,
    pay_type          VARCHAR(150)                          NULL,
    account_number    VARCHAR(150)                          NULL,
    update_log        VARCHAR(255)                          NULL
);

CREATE TABLE "order"
(
    individual_id  VARCHAR(36)                           NULL,
    id             VARCHAR(36)                           NOT NULL PRIMARY KEY,
    order_code     VARCHAR(20)                           NULL,
    customer_id    VARCHAR(36)                           NULL,
    total_fee      DOUBLE PRECISION                      NULL,
    currency       VARCHAR(20)                           NULL,
    area_code      VARCHAR(20)                           NULL,
    province       VARCHAR(50)                           NULL,
    district       VARCHAR(50)                           NULL,
    precinct       VARCHAR(50)                           NULL,
    status         SMALLINT                              NULL,
    create_at      TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP NOT NULL,
    create_by      VARCHAR(30)                           NULL,
    update_at      TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP NULL,
    update_by      VARCHAR(30)                           NULL,
    detail_address VARCHAR(255)                          NULL,
    state          INT                                   NULL,
    description    VARCHAR(500)                          NULL,
    type           VARCHAR(25)                           NULL,
    id_no          VARCHAR(36)                           NULL,
    name           VARCHAR(500)                          NULL,
    email          VARCHAR(50)                           NULL,
    phone          VARCHAR(20)                           NULL,
    company_name   VARCHAR(100)                          NULL,
    logs           VARCHAR(1000)                         NULL
);

CREATE TABLE order_data
(
    order_id   VARCHAR(200) NULL,
    data       TEXT         NULL,
    order_type VARCHAR(200) NULL,
    status     INT          NULL,
    create_by  VARCHAR(200) NULL,
    create_at  TIMESTAMPTZ  NULL,
    update_by  VARCHAR(200) NULL,
    update_at  TIMESTAMPTZ  NULL
);

CREATE INDEX pg_order_data_orderId
    ON order_data (order_id);

CREATE TABLE order_ext
(
    id        VARCHAR(36)                           NOT NULL PRIMARY KEY,
    order_id  VARCHAR(36)                           NULL,
    code      VARCHAR(50)                           NULL,
    value     VARCHAR(100)                          NULL,
    status    SMALLINT                              NULL,
    create_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP NULL,
    create_by VARCHAR(30)                           NULL,
    update_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP NULL,
    update_by VARCHAR(30)                           NULL
);

CREATE TABLE order_file
(
    id        VARCHAR(36)                           NOT NULL PRIMARY KEY,
    order_id  VARCHAR(36)                           NULL,
    type      VARCHAR(200)                          NULL,
    path      VARCHAR(200)                          NULL,
    status    SMALLINT    DEFAULT 1                 NULL,
    create_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP NULL,
    create_by VARCHAR(30)                           NULL,
    update_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP NULL,
    update_by VARCHAR(30)                           NULL
);

CREATE TABLE order_item
(
    id                    VARCHAR(36)                           NOT NULL PRIMARY KEY,
    product_id            VARCHAR(255)                          NULL,
    order_id              VARCHAR(36)                           NULL,
    name                  VARCHAR(255)                          NULL,
    currency              VARCHAR(50)                           NULL,
    description           VARCHAR(2000)                         NULL,
    review_content        VARCHAR(2000)                         NULL,
    rating                INT                                   NULL,
    quantity              INT                                   NULL,
    status                SMALLINT                              NULL,
    price                 DOUBLE PRECISION                      NULL,
    update_at             TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP NULL,
    update_by             VARCHAR(255)                          NULL,
    create_at             TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP NULL,
    create_by             VARCHAR(255)                          NULL,
    duration              VARCHAR(50)                           NULL,
    origin_price          DOUBLE PRECISION                      NULL,
    telecom_service_id    VARCHAR(36)                           NULL,
    telecom_service_name  VARCHAR(200)                          NULL,
    order_code            VARCHAR(20)                           NULL,
    subscriber_id         VARCHAR(50)                           NULL,
    is_bundle             SMALLINT                              NULL,
    account_id            VARCHAR(50)                           NULL,
    state                 INT                                   NULL,
    product_code          VARCHAR(50)                           NULL,
    telecom_service_alias VARCHAR(100)                          NULL
);

CREATE TABLE characteristic
(
    id            VARCHAR(36)                           NOT NULL PRIMARY KEY,
    name          VARCHAR(50)                           NULL,
    value_type    VARCHAR(20)                           NULL,
    value         VARCHAR(200)                          NULL,
    code          VARCHAR(50)                           NULL,
    order_item_id VARCHAR(36)                           NULL,
    create_at     TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP NULL,
    create_by     VARCHAR(30)                           NULL,
    update_at     TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP NULL,
    update_by     VARCHAR(30)                           NULL,
    status        SMALLINT                              NULL,
    CONSTRAINT characteristic_order_item_id_fk
        FOREIGN KEY (order_item_id) REFERENCES order_item (id)
);

CREATE INDEX order_item_order_id_fk
    ON order_item (order_id);

CREATE TABLE order_type
(
    id        SERIAL PRIMARY KEY,
    name      VARCHAR(255) NULL,
    alias     VARCHAR(50)  NULL,
    status    SMALLINT     NULL,
    create_at TIMESTAMPTZ  NULL,
    create_by VARCHAR(30)  NULL,
    update_at TIMESTAMPTZ  NULL,
    update_by VARCHAR(30)  NULL
);

CREATE TABLE order_field_config
(
    id             SERIAL PRIMARY KEY,
    name           INTEGER      NULL, -- 'ten khach hang'
    email          INTEGER      NULL, -- 'email khach hang'
    phone          INTEGER      NULL, -- 'so dien thoai khach hang'
    status         SMALLINT     NULL,
    create_at      TIMESTAMP    NULL,
    create_by      VARCHAR(30)  NULL,
    update_at      TIMESTAMP    NULL,
    update_by      VARCHAR(30)  NULL,
    area_code      INTEGER      NULL, -- 'ma khu vuc khach hang'
    detail_address INTEGER      NULL, -- 'dia chi cu the'
    order_type_id  INTEGER      NULL, -- 'loai don hang'
    service_id     VARCHAR(20)  NULL, -- 'loai dich vu telecom'
    from_staff     INTEGER      NULL, -- 'ma nhan vien gioi thieu'
    service_alias  VARCHAR(100) NULL, -- 'alias order_item'
    am_staff       VARCHAR(50)  NULL, -- 'ma AM ho tro'
    CONSTRAINT order_field_config_order_type_id_fk FOREIGN KEY (order_type_id)
        REFERENCES order_type (id)
);

CREATE TABLE partner_license_key
(
    id              VARCHAR(36)  NULL,
    service_alias   VARCHAR(20)  NULL, -- alias dich vu
    user_name       VARCHAR(100) NULL, -- user name dang nhap
    user_id         VARCHAR(36)  NULL, -- id user
    organization_id VARCHAR(36)  NULL, -- id cong ty
    license_key     VARCHAR(50)  NULL, -- ma dac biet dang tenVietTat_random6kytuso
    status          SMALLINT     NULL, -- trang thai 1 - hieu luc, 0 - khong hieu luc
    create_at       TIMESTAMP    NULL,
    create_by       VARCHAR(30)  NULL,
    update_at       TIMESTAMP    NULL,
    update_by       VARCHAR(30)  NULL
)