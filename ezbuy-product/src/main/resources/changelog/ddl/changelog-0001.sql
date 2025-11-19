-- database changelog
-- PRODUCT DATABASE POSTGRESQL

-- Tạo bảng sync_history
CREATE TABLE sync_history
(
    id               VARCHAR(36) PRIMARY KEY NOT NULL,
    org_id           VARCHAR(36)             NULL,
    id_no            VARCHAR(30)             NULL,
    action           VARCHAR(10)             NULL,
    service_type     VARCHAR(20)             NULL,
    dst_service      VARCHAR(200)            NULL,
    sync_type        VARCHAR(10)             NULL,
    object_type      VARCHAR(10)             NULL,
    ids              VARCHAR(200)            NULL,
    request_id       VARCHAR(36)             NULL,
    response_data    TEXT                    NULL,
    response_at      TIMESTAMP               NULL,
    error_code       VARCHAR(20)             NULL,
    response_message VARCHAR(200)            NULL,
    retry            INT                     NULL,
    state            VARCHAR(10)             NULL,
    status           INTEGER                 NULL,
    create_at        TIMESTAMP               NULL,
    create_by        VARCHAR(30)             NULL,
    update_at        TIMESTAMP               NULL,
    update_by        VARCHAR(30)             NULL,
    sync_trans_id    VARCHAR(36)             NULL -- 'Mã transId lấy từ databus để định danh luồng đồng bộ'
);

-- Tạo bảng sync_history_detail
CREATE TABLE sync_history_detail
(
    id              VARCHAR(36) PRIMARY KEY NOT NULL,
    target_id       VARCHAR(36)             NULL,
    status          INTEGER                 NULL, -- 'Trạng thái 1 - hiệu lực, 0 - không hiệu lực'
    create_at       TIMESTAMP               NULL,
    create_by       VARCHAR(30)             NULL,
    update_at       TIMESTAMP               NULL,
    update_by       VARCHAR(30)             NULL,
    sync_trans_id   VARCHAR(36)             NULL, -- 'Mã transId lấy từ databus để định danh luồng đồng bộ'
    sync_history_id VARCHAR(36)             NULL  -- 'ID bảng sync_history'
);

-- Tạo bảng voucher
CREATE TABLE voucher
(
    id              VARCHAR(36) PRIMARY KEY NOT NULL,
    code            VARCHAR(200)            NULL,
    voucher_type_id VARCHAR(36)             NULL, -- 'ID bảng voucher_type'
    batch_id        VARCHAR(36)             NULL, -- 'ID bảng voucher_batch'
    expired_date    TIMESTAMP               NULL, -- 'Thời điểm hết hạn'
    expired_period  INT                     NULL, -- 'Thời gian sử dụng tính theo ngày'
    state           VARCHAR(20)             NULL, -- 'new, locked, used, inactive'
    create_at       TIMESTAMP               NULL,
    create_by       VARCHAR(50)             NULL,
    update_at       TIMESTAMP               NULL,
    update_by       VARCHAR(50)             NULL
);

-- Tạo bảng voucher_type
CREATE TABLE voucher_type
(
    id             VARCHAR(36)  NOT NULL PRIMARY KEY,
    code           VARCHAR(200) NULL,
    name           VARCHAR(200) NULL, -- 'Tên voucher type'
    priority_level INT          NULL,
    description    VARCHAR(200) NULL,
    action_type    VARCHAR(20)  NULL,
    action_value   VARCHAR(200) NULL,
    payment        VARCHAR(200) NULL, -- 'Phương thức thanh toán'
    state          VARCHAR(20)  NULL,
    status         INTEGER      NULL,
    create_at      TIMESTAMP    NULL,
    create_by      VARCHAR(50)  NULL,
    update_at      TIMESTAMP    NULL,
    update_by      VARCHAR(50)  NULL,
    condition_use  TEXT         NULL  -- 'Điều kiện sử dụng voucher'
);
COMMENT ON TABLE voucher_type IS 'Thong bang voucher_type';
COMMENT ON COLUMN voucher_type.name IS 'Ten voucher type';
COMMENT ON COLUMN voucher_type.payment IS 'Phuong thuc thanh toan';
COMMENT ON COLUMN voucher_type.condition_use IS 'Dieu kien su dung voucher';

-- Tạo bảng voucher_use
CREATE TABLE voucher_use
(
    id              VARCHAR(36)  NOT NULL PRIMARY KEY,
    voucher_id      VARCHAR(36)  NULL,     -- 'ID bảng voucher'
    user_id         VARCHAR(36)  NULL,     -- 'ID user đăng nhập'
    username        VARCHAR(200) NULL,     -- 'Tên người sử dụng voucher'
    system_type     VARCHAR(200) NULL,     -- 'Hệ thống áp dụng voucher'
    create_date     TIMESTAMP    NOT NULL, -- 'Ngày bắt đầu voucher'
    expired_date    TIMESTAMP    NULL,     -- 'Ngày hết hạn sau khi đã tính toán'
    state           VARCHAR(20)  NULL,     -- 'preActive, active, inactive, used'
    source_order_id VARCHAR(36)  NULL,     -- 'ID bảng sme_order.order'
    create_at       TIMESTAMP    NULL,
    create_by       VARCHAR(50)  NULL,
    update_at       TIMESTAMP    NULL,
    update_by       VARCHAR(50)  NULL
);
COMMENT ON TABLE voucher_use IS 'Thong bang voucher_use';
COMMENT ON COLUMN voucher_use.voucher_id IS 'ID bang voucher';
COMMENT ON COLUMN voucher_use.user_id IS 'ID user dang nhap';
COMMENT ON COLUMN voucher_use.username IS 'Ten nguoi su dung voucher';
COMMENT ON COLUMN voucher_use.system_type IS 'He thong ap dung voucher';
COMMENT ON COLUMN voucher_use.create_date IS 'Ngay bat dau voucher';
COMMENT ON COLUMN voucher_use.expired_date IS 'Ngay het han sau khi da tinh toan';
COMMENT ON COLUMN voucher_use.state IS 'preActive, active, inactive, used';
COMMENT ON COLUMN voucher_use.source_order_id IS 'ID bảng order';

-- Tạo index cho bảng voucher_use
CREATE INDEX i_vu_ui
    ON voucher_use (user_id);

CREATE INDEX i_vu_vi
    ON voucher_use (voucher_id);

-- Tạo bảng voucher_batch
CREATE TABLE voucher_batch
(
    id              VARCHAR(36)  NOT NULL PRIMARY KEY,
    code            VARCHAR(200) NULL,
    description     VARCHAR(200) NULL,
    voucher_type_id VARCHAR(36)  NULL,
    quantity        INT          NULL,
    expired_date    TIMESTAMP    NULL,
    expired_period  INT          NULL,
    state           VARCHAR(20)  NULL,
    create_at       TIMESTAMP    NULL,
    create_by       VARCHAR(50)  NULL,
    update_at       TIMESTAMP    NULL,
    update_by       VARCHAR(50)  NULL
);

-- Tạo bảng voucher_transaction
CREATE TABLE voucher_transaction
(
    id               VARCHAR(36)  NOT NULL PRIMARY KEY,
    voucher_id       VARCHAR(36)  NULL, -- 'ID bảng voucher'
    user_id          VARCHAR(36)  NULL, -- 'ID user đăng nhập'
    transaction_code VARCHAR(36)  NULL, -- 'Mã đơn hàng sử dụng voucher'
    transaction_date TIMESTAMP    NULL,
    transaction_type VARCHAR(200) NULL,
    amount           INT          NULL, -- 'Số tiền được giảm sau khi sử dụng voucher'
    state            VARCHAR(20)  NULL,
    create_at        TIMESTAMP    NULL,
    create_by        VARCHAR(50)  NULL,
    update_at        TIMESTAMP    NULL,
    update_by        VARCHAR(50)  NULL
);
COMMENT ON TABLE voucher_transaction IS 'Thong bang voucher_transaction';
COMMENT ON COLUMN voucher_transaction.voucher_id IS 'ID bang voucher';
COMMENT ON COLUMN voucher_transaction.user_id IS 'ID user dang nhap';
COMMENT ON COLUMN voucher_transaction.transaction_code IS 'Ma don hang su dung voucher';
COMMENT ON COLUMN voucher_transaction.amount IS 'So tien duoc giam sau khi su dung voucher';

-- Tạo bảng inventory
CREATE TABLE inventory
(
    id        VARCHAR(36)  NOT NULL PRIMARY KEY,
    code      VARCHAR(100) NOT NULL, -- 'Mã kho'
    name      VARCHAR(100) NULL,     -- 'Tên kho'
    status    INTEGER      NOT NULL, -- 'Trạng thái'
    create_by VARCHAR(50)  NULL,
    create_at TIMESTAMP    NULL,
    update_at VARCHAR(100) NULL,
    update_by VARCHAR(100) NULL
);
COMMENT ON TABLE inventory IS 'Thong tin kho';
COMMENT ON COLUMN inventory.code IS 'Ma kho';
COMMENT ON COLUMN inventory.name IS 'Ten kho';
COMMENT ON COLUMN inventory.status IS 'Trang thai';


-- product
CREATE TABLE product
(
    id              VARCHAR(36) PRIMARY KEY NOT NULL,
    category_id     VARCHAR(36)             NULL,
    code            VARCHAR(15)             NULL, -- mã hàng hóa
    name            VARCHAR(500)            NULL, -- tên hàng hóa
    price_import    DOUBLE PRECISION        NULL, -- đơn giá nhập
    price_export    DOUBLE PRECISION        NULL, -- đơn giá bán
    unit            VARCHAR(150)            NULL, -- đơn vị tính
    tax_ratio       VARCHAR(50)             NULL, -- thuế GTGT
    discount        DOUBLE PRECISION        NULL, -- chiết khấu
    revenue_ratio   INT                     NULL, -- tỷ lệ % theo doanh thu
    status          INTEGER                 NULL, -- trạng thái: 1 = hiệu lực, 0 = không hiệu lực
    lock_status     INTEGER                 NULL, -- trạng thái khóa: 1 = đã khóa, 0 = không khóa
    create_unit     VARCHAR(255)            NULL, -- tên đơn vị tạo
    create_at       TIMESTAMP               NULL, -- thời gian tạo
    create_by       VARCHAR(100)            NULL, -- người tạo
    update_at       TIMESTAMP               NULL, -- thời gian cập nhật
    update_by       VARCHAR(100)            NULL, -- người cập nhật
    organization_id VARCHAR(36)             NULL  -- mã tổ chức tạo
);
COMMENT ON TABLE product IS 'Thong tin bang product';
COMMENT ON COLUMN product.code IS 'Ma hang hoa';
COMMENT ON COLUMN product.name IS 'Ten hang hoa';
COMMENT ON COLUMN product.price_import IS 'Don gia nhap';
COMMENT ON COLUMN product.price_export IS 'Don gia ban';
COMMENT ON COLUMN product.unit IS 'Don vi tinh';
COMMENT ON COLUMN product.tax_ratio IS 'Thue GTGT';
COMMENT ON COLUMN product.discount IS 'Chiet khau';
COMMENT ON COLUMN product.revenue_ratio IS 'Ti le phan tram theo doanh thu';
COMMENT ON COLUMN product.status IS 'Trang thai: 1 = hieu luc, 0 = khong hieu luc';
COMMENT ON COLUMN product.lock_status IS 'Trang thai: 1 = da khoa, 0 = khong khoa';
COMMENT ON COLUMN product.create_unit IS 'Ten don vi tao';
COMMENT ON COLUMN product.organization_id IS 'Ma to chuc tao';

CREATE TABLE brand
(
    id        VARCHAR(36) PRIMARY KEY NOT NULL,
    name      VARCHAR(100)            NOT NULL UNIQUE,
    logo_url  TEXT                    NULL,
    create_at TIMESTAMP               NULL,
    create_by VARCHAR(100)            NULL,
    update_at TIMESTAMP               NULL,
    update_by VARCHAR(100)            NULL
);

CREATE TABLE product_image
(
    id         VARCHAR(36) PRIMARY KEY NOT NULL,
    product_id VARCHAR(36)             NOT NULL,
    image_url  TEXT                    NULL,
    is_primary BOOLEAN DEFAULT FALSE,
    create_at  TIMESTAMP               NULL,
    create_by  VARCHAR(100)            NULL,
    update_at  TIMESTAMP               NULL,
    update_by  VARCHAR(100)            NULL,
    FOREIGN KEY (product_id) REFERENCES product (id) ON DELETE CASCADE
);

CREATE TABLE product_reviews
(
    id         VARCHAR(36) PRIMARY KEY NOT NULL,
    product_id VARCHAR(36)             NOT NULL,
    user_id    VARCHAR(36)             NULL,
    rating     INT CHECK (rating BETWEEN 1 AND 5),
    review     TEXT,
    create_at  TIMESTAMP               NULL,
    create_by  VARCHAR(100)            NULL,
    update_at  TIMESTAMP               NULL,
    update_by  VARCHAR(100)            NULL,
    FOREIGN KEY (product_id) REFERENCES product (id) ON DELETE CASCADE
);

CREATE TABLE product_tags
(
    product_id VARCHAR(36) NOT NULL,
    tag        VARCHAR(50) NOT NULL,
    PRIMARY KEY (product_id, tag),
    FOREIGN KEY (product_id) REFERENCES product (id) ON DELETE CASCADE
);

CREATE INDEX idx_products_name ON product (name);
CREATE INDEX idx_products_category_id ON product (category_id);
CREATE INDEX idx_price_export ON product (price_export);

-- Dùng để quản lý các phiên bản khác nhau của sản phẩm (ví dụ: cùng một sản phẩm nhưng có các màu sắc, kích thước khác nhau).
CREATE TABLE product_variants
(
    id             VARCHAR(36) PRIMARY KEY NOT NULL,
    product_id     VARCHAR(36)             NOT NULL,
    variant_name   VARCHAR(100)            NOT NULL,              -- Tên phiên bản (ví dụ: Màu sắc, Kích thước)
    variant_value  VARCHAR(100)            NOT NULL,              -- Giá trị của phiên bản (ví dụ: Đỏ, L, XL)
    price          NUMERIC(15, 2)                   DEFAULT NULL, -- Giá khác nếu phiên bản có giá riêng
    stock_quantity INT                     NOT NULL DEFAULT 0,
    created_at     TIMESTAMP                        DEFAULT CURRENT_TIMESTAMP,
    updated_at     TIMESTAMP                        DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (product_id) REFERENCES product (id) ON DELETE CASCADE
);
COMMENT ON COLUMN product_variants.variant_name IS 'Ten phien ban (vi du: Mau sac, Kich thuoc)';
COMMENT ON COLUMN product_variants.variant_value IS 'Gia tri cua phien ban (vi du: Đo, L, XL)';
COMMENT ON COLUMN product_variants.price IS 'Gia khac neu phien ban co gia rieng';

-- Lưu trữ danh sách yêu thích của khách hàng.
CREATE TABLE wishlist
(
    user_id    VARCHAR(36) NOT NULL,
    product_id VARCHAR(36) NOT NULL,
    added_at   TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (user_id, product_id),
    FOREIGN KEY (product_id) REFERENCES product (id) ON DELETE CASCADE
);

CREATE TABLE product_ratings_summary
(
    product_id       VARCHAR(36) NOT NULL,
    average_rating   NUMERIC(3, 2) DEFAULT 0.00,
    total_reviews    INT           DEFAULT 0,
    last_reviewed_at TIMESTAMP     DEFAULT NULL,
    FOREIGN KEY (product_id) REFERENCES product (id) ON DELETE CASCADE
);

-- san pham lien quan
CREATE TABLE related_products
(
    product_id         VARCHAR(36) NOT NULL,
    related_product_id VARCHAR(36) NOT NULL,
    PRIMARY KEY (product_id, related_product_id),
    FOREIGN KEY (product_id) REFERENCES product (id) ON DELETE CASCADE,
    FOREIGN KEY (related_product_id) REFERENCES product (id) ON DELETE CASCADE
);


