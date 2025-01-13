-- database changelog
-- Date: 2024-07-27

-- table user_otp
CREATE TABLE user_otp
(
    id        VARCHAR(36) PRIMARY KEY NOT NULL,
    type      VARCHAR(20) NULL,
    email     VARCHAR(200) NULL,
    otp       VARCHAR(25) NULL,
    exp_time  TIMESTAMP NULL,
    tries     INT NULL,
    status    SMALLINT DEFAULT 1 NULL,
    create_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NULL,
    create_by VARCHAR(30) NULL,
    update_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NULL,
    update_by VARCHAR(30) NULL
);
COMMENT ON TABLE user_otp IS 'Bang luu tru otp';
COMMENT ON COLUMN user_otp.type IS 'Loai OTP';
COMMENT ON COLUMN user_otp.email IS 'Email nguoi dung';
COMMENT ON COLUMN user_otp.otp IS 'OTP';
COMMENT ON COLUMN user_otp.exp_time IS 'Thoi gian het han OTP';
COMMENT ON COLUMN user_otp.tries IS 'So lan thu';
COMMENT ON COLUMN user_otp.status IS 'Trang thai ban ghi';
COMMENT ON COLUMN user_otp.create_at IS 'Thoi gian tao';
COMMENT ON COLUMN user_otp.create_by IS 'Nguoi tao';
COMMENT ON COLUMN user_otp.update_at IS 'Thoi gian cap nhat';
COMMENT ON COLUMN user_otp.update_by IS 'Nguoi cap nhat';

-- table individual
CREATE TABLE individual
(
    id                    VARCHAR(36) PRIMARY KEY NOT NULL,
    user_id               VARCHAR(50) NULL,
    username              VARCHAR(50) NULL,
    name                  VARCHAR(255) NULL,
    image                 VARCHAR(500) NULL,
    gender                VARCHAR(10) NULL,
    birthday              TIMESTAMP NULL,
    email                 VARCHAR(200) NULL,
    phone                 VARCHAR(20) NULL,
    address               VARCHAR(300) NULL,
    province_code         VARCHAR(50) NULL,
    district_code         VARCHAR(50) NULL,
    precinct_code         VARCHAR(50) NULL,
    status                SMALLINT DEFAULT 1 NULL,
    create_at             TIMESTAMP DEFAULT CURRENT_TIMESTAMP NULL,
    create_by             VARCHAR(255) NULL,
    update_at             TIMESTAMP DEFAULT CURRENT_TIMESTAMP NULL,
    update_by             VARCHAR(255) NULL,
    password_change       SMALLINT DEFAULT 1 NULL,
    code                  VARCHAR(100) NULL,
    posision_code         VARCHAR(20) NULL,
    type                  VARCHAR(100) NULL,
    email_account         VARCHAR(250) NULL,
    position_code         VARCHAR(20) NULL,
    probation_day         TIMESTAMP NULL,
    start_working_day     TIMESTAMP NULL,
    national              VARCHAR(255) NULL,
    last_working_day      TIMESTAMP NULL,
    send_account_to_phone SMALLINT DEFAULT 0 NULL,
    phone_account         VARCHAR(20) NULL,
    status_account        SMALLINT NULL
);
COMMENT ON TABLE individual IS 'Thong tin ca nhan';
COMMENT ON COLUMN individual.user_id IS 'user_id cua ca nhan (mapping voi keycloak)';
COMMENT ON COLUMN individual.username IS 'Ten dang nhap khach hang';
COMMENT ON COLUMN individual.name IS 'Ten ca nhan';
COMMENT ON COLUMN individual.image IS 'Anh dai dien';
COMMENT ON COLUMN individual.gender IS 'Gioi tinh';
COMMENT ON COLUMN individual.birthday IS 'Ngay sinh';
COMMENT ON COLUMN individual.email IS 'Dia chi email';
COMMENT ON COLUMN individual.phone IS 'So dien thoai';
COMMENT ON COLUMN individual.address IS 'Dia chi';
COMMENT ON COLUMN individual.province_code IS 'Ma tinh';
COMMENT ON COLUMN individual.district_code IS 'Ma huyen';
COMMENT ON COLUMN individual.precinct_code IS 'Ma xa';
COMMENT ON COLUMN individual.status IS 'Trang thai ban ghi';
COMMENT ON COLUMN individual.create_at IS 'Thoi gian tao';
COMMENT ON COLUMN individual.create_by IS 'Nguoi tao';
COMMENT ON COLUMN individual.update_at IS 'Thoi gian cap nhat';
COMMENT ON COLUMN individual.update_by IS 'Nguoi cap nhat';
COMMENT ON COLUMN individual.password_change IS 'Trang thai thay doi mat khau';
COMMENT ON COLUMN individual.code IS 'Ma ca nhan';
COMMENT ON COLUMN individual.posision_code IS 'Ma vi tri lam viec';
COMMENT ON COLUMN individual.type IS 'Loai ca nhan';
COMMENT ON COLUMN individual.email_account IS 'Email tai khoan';
COMMENT ON COLUMN individual.position_code IS 'Ma vi tri chinh thuc';
COMMENT ON COLUMN individual.probation_day IS 'Ngay thu viec hoac ngay bat dau lam viec';
COMMENT ON COLUMN individual.start_working_day IS 'Ngay chinh thuc hoac ngay ky hop dong';
COMMENT ON COLUMN individual.national IS 'Quoc tich';
COMMENT ON COLUMN individual.last_working_day IS 'Ngay nghi viec';
COMMENT ON COLUMN individual.send_account_to_phone IS 'Co gui tai khoan den so dien thoai hay khong';
COMMENT ON COLUMN individual.phone_account IS 'So dien thoai tai khoan';
COMMENT ON COLUMN individual.status_account IS 'Trang thai tai khoan tren keycloak';

-- table user_credential
CREATE TABLE user_credential
(
    id          VARCHAR(36) PRIMARY KEY NOT NULL,
    user_id     VARCHAR(50) NULL,
    username    VARCHAR(50) NULL,
    hash_pwd    TEXT NULL,
    status      SMALLINT DEFAULT 1 NULL,
    create_at   TIMESTAMP DEFAULT CURRENT_TIMESTAMP NULL,
    create_by   VARCHAR(255) NULL,
    update_at   TIMESTAMP DEFAULT CURRENT_TIMESTAMP NULL,
    update_by   VARCHAR(255) NULL,
    pwd_changed SMALLINT DEFAULT 1 NULL
);
COMMENT ON TABLE user_credential IS 'Bang luu mat khau cua nguoi dung (hashed by password)';
COMMENT ON COLUMN user_credential.id IS 'ID cua bang';
COMMENT ON COLUMN user_credential.user_id IS 'user_id cua ca nhan (mapping voi keycloak)';
COMMENT ON COLUMN user_credential.username IS 'Ten dang nhap cua nguoi dung';
COMMENT ON COLUMN user_credential.hash_pwd IS 'Password hashed by HUB-SME';
COMMENT ON COLUMN user_credential.status IS 'Trang thai ban ghi';
COMMENT ON COLUMN user_credential.create_at IS 'Thoi gian tao';
COMMENT ON COLUMN user_credential.create_by IS 'Nguoi tao';
COMMENT ON COLUMN user_credential.update_at IS 'Thoi gian cap nhat';
COMMENT ON COLUMN user_credential.update_by IS 'Nguoi cap nhat';
COMMENT ON COLUMN user_credential.pwd_changed IS 'Da thay doi mat khau';

-- table action_log
CREATE TABLE action_log
(
    id         VARCHAR(36) PRIMARY KEY NOT NULL,
    user_id    VARCHAR(36) NULL,
    username   VARCHAR(255) NULL,
    ip         VARCHAR(20) NULL,
    type       VARCHAR(20) NULL,
    create_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP NULL,
    user_agent VARCHAR(200) NULL
);
COMMENT ON TABLE action_log IS 'Bang luu log tac dong nguoi dung';
COMMENT ON COLUMN action_log.id IS 'ID cua bang';
COMMENT ON COLUMN action_log.user_id IS 'Dinh danh nguoi dung';
COMMENT ON COLUMN action_log.username IS 'Ten nguoi dung';
COMMENT ON COLUMN action_log.ip IS 'IP cua nguoi dung';
COMMENT ON COLUMN action_log.type IS 'Loai tac dong';
COMMENT ON COLUMN action_log.create_at IS 'Thoi gian tac dong';
COMMENT ON COLUMN action_log.user_agent IS 'User agent cua nguoi dung';

CREATE INDEX idx_action_log_user_id ON action_log (user_id);
CREATE INDEX idx_action_log_create_at ON action_log (create_at);

-- table organization
CREATE TABLE organization
(
    id             VARCHAR(36) PRIMARY KEY NOT NULL,
    name           VARCHAR(255) NULL,
    image          VARCHAR(500) NULL,
    business_type  VARCHAR(255) NULL,
    founding_date  TIMESTAMP NULL,
    email          VARCHAR(200) NULL,
    phone          VARCHAR(12) NULL,
    province_code  VARCHAR(5) NULL,
    district_code  VARCHAR(5) NULL,
    precinct_code  VARCHAR(5) NULL,
    street_block   VARCHAR(200) NULL,
    state          SMALLINT NULL,
    status         SMALLINT NULL,
    create_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP NULL,
    create_by      VARCHAR(255) NULL,
    update_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP NULL,
    update_by      VARCHAR(255) NULL,
    tax_department VARCHAR(255) NULL,
    org_type       VARCHAR(20) NULL,
    nation         VARCHAR(150) NULL
);
COMMENT ON TABLE organization IS 'Bang luu thong tin doanh nghiep';
COMMENT ON COLUMN organization.id IS 'ID cua bang';
COMMENT ON COLUMN organization.name IS 'Ten doanh nghiep';
COMMENT ON COLUMN organization.image IS 'Anh doanh nghiep';
COMMENT ON COLUMN organization.business_type IS 'Loai hinh kinh doanh';
COMMENT ON COLUMN organization.founding_date IS 'Ngay thanh lap';
COMMENT ON COLUMN organization.email IS 'Dia chi email';
COMMENT ON COLUMN organization.phone IS 'So dien thoai';
COMMENT ON COLUMN organization.province_code IS 'Ma tinh';
COMMENT ON COLUMN organization.district_code IS 'Ma huyen';
COMMENT ON COLUMN organization.precinct_code IS 'Ma xa';
COMMENT ON COLUMN organization.street_block IS 'Ma duong';
COMMENT ON COLUMN organization.state IS 'Trang thai doanh nghiep';
COMMENT ON COLUMN organization.status IS 'Trang thai cua ban ghi';
COMMENT ON COLUMN organization.create_at IS 'Thoi gian tao';
COMMENT ON COLUMN organization.create_by IS 'Nguoi tao';
COMMENT ON COLUMN organization.update_at IS 'Thoi gian cap nhat';
COMMENT ON COLUMN organization.update_by IS 'Nguoi cap nhat';
COMMENT ON COLUMN organization.tax_department IS 'Chi cuc thue';
COMMENT ON COLUMN organization.org_type IS 'Loai doanh nghiep';
COMMENT ON COLUMN organization.nation IS 'Alias dich vu do BCCS quy dinh';

-- table individual_organization_permissions
CREATE TABLE individual_organization_permissions
(
    id              VARCHAR(36) PRIMARY KEY NOT NULL,
    user_id         VARCHAR(36) NULL,
    organization_id VARCHAR(36) NULL,
    status          SMALLINT NULL,
    create_at       TIMESTAMP DEFAULT CURRENT_TIMESTAMP NULL,
    create_by       VARCHAR(255) NULL,
    update_at       TIMESTAMP DEFAULT CURRENT_TIMESTAMP NULL,
    update_by       VARCHAR(255) NULL,
    client_id       VARCHAR(100) NULL,
    individual_id   VARCHAR(36) NULL,
    CONSTRAINT individual_organization_permissions_organization_id_fk
        FOREIGN KEY (organization_id) REFERENCES organization (id)
);
COMMENT ON TABLE individual_organization_permissions IS 'Bang phan quyen ca nhan voi to chuc';
COMMENT ON COLUMN individual_organization_permissions.id IS 'ID cua bang';
COMMENT ON COLUMN individual_organization_permissions.user_id IS 'Khoa ngoai voi individual table';
COMMENT ON COLUMN individual_organization_permissions.organization_id IS 'Khoa ngoai voi organization table';
COMMENT ON COLUMN individual_organization_permissions.status IS 'Trang thai cua ban ghi';
COMMENT ON COLUMN individual_organization_permissions.create_at IS 'Thoi gian tao';
COMMENT ON COLUMN individual_organization_permissions.create_by IS 'Nguoi tao';
COMMENT ON COLUMN individual_organization_permissions.update_at IS 'Thoi gian cap nhat';
COMMENT ON COLUMN individual_organization_permissions.update_by IS 'Nguoi cap nhat';
COMMENT ON COLUMN individual_organization_permissions.client_id IS 'Client_id cua dich vu';
COMMENT ON COLUMN individual_organization_permissions.individual_id IS 'ID ca nhan';

CREATE INDEX individual_organization_permissions_user_id_idx
    ON individual_organization_permissions (user_id);

-- table permission_policy
CREATE TABLE permission_policy
(
    id                                     VARCHAR(36) PRIMARY KEY NOT NULL,
    type                                   VARCHAR(36) NULL,
    code                                   VARCHAR(50) NULL,
    individual_organization_permissions_id VARCHAR(36) NULL,
    status                                 SMALLINT NULL,
    create_at                              TIMESTAMP DEFAULT CURRENT_TIMESTAMP NULL,
    create_by                              VARCHAR(255) NULL,
    update_at                              TIMESTAMP DEFAULT CURRENT_TIMESTAMP NULL,
    update_by                              VARCHAR(255) NULL,
    keycloak_id                            VARCHAR(50) NULL,
    policy_id                              VARCHAR(36) NULL,
    keycloak_name                          VARCHAR(255) NULL,
    CONSTRAINT permission_policy_individual_organization_permissions_id_fk
        FOREIGN KEY (individual_organization_permissions_id) REFERENCES individual_organization_permissions (id)
);
COMMENT ON TABLE permission_policy IS 'Danh sach cac role, group...';
COMMENT ON COLUMN permission_policy.id IS 'ID cua bang';
COMMENT ON COLUMN permission_policy.type IS 'Loai quyen';
COMMENT ON COLUMN permission_policy.code IS 'Ma code cua quyen';
COMMENT ON COLUMN permission_policy.individual_organization_permissions_id IS 'Khoa ngoai voi bang individual_organization_permissions table';
COMMENT ON COLUMN permission_policy.status IS 'Trang thai cua ban ghi';
COMMENT ON COLUMN permission_policy.create_at IS 'Thoi gian tao';
COMMENT ON COLUMN permission_policy.create_by IS 'Nguoi tao';
COMMENT ON COLUMN permission_policy.update_at IS 'Thoi gian cap nhat';
COMMENT ON COLUMN permission_policy.update_by IS 'Nguoi cap nhat';
COMMENT ON COLUMN permission_policy.keycloak_id IS 'ID cua nhom quyen hoac nhom nguoi dung tren keycloak ( type ROLE: nhom quyen, type GROUP: nhom nguoi dung )';
COMMENT ON COLUMN permission_policy.policy_id IS 'PolicyId tren keycloak';
COMMENT ON COLUMN permission_policy.keycloak_name IS 'Ten cua nhom quyen tren keycloak';

-- table user_profile
CREATE TABLE user_profile
(
    id             VARCHAR(36) PRIMARY KEY NOT NULL,
    image          VARCHAR(200) NULL,
    company_name   VARCHAR(255) NULL,
    phone          VARCHAR(12) NULL,
    tax_code       VARCHAR(13) NULL,
    tax_department VARCHAR(255) NULL,
    representative VARCHAR(255) NULL,
    founding_date  DATE NULL,
    business_type  VARCHAR(255) NULL,
    province_code  VARCHAR(5) NULL,
    district_code  VARCHAR(5) NULL,
    precinct_code  VARCHAR(5) NULL,
    street_block   VARCHAR(20) NULL,
    create_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP NULL,
    update_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP NULL,
    user_id        VARCHAR(36) NULL,
    create_by      VARCHAR(255) NULL,
    update_by      VARCHAR(255) NULL,
    trust_status   VARCHAR(20) NULL,
    CONSTRAINT user_id UNIQUE (user_id)
);
COMMENT ON TABLE user_profile IS 'Bang luu thong tin nguoi dung';
COMMENT ON COLUMN user_profile.id IS 'ID cua bang';
COMMENT ON COLUMN user_profile.image IS 'Anh dai dien';
COMMENT ON COLUMN user_profile.company_name IS 'Ten cong ty';
COMMENT ON COLUMN user_profile.phone IS 'So dien thoai cong ty';
COMMENT ON COLUMN user_profile.tax_code IS 'Ma so thue';
COMMENT ON COLUMN user_profile.tax_department IS 'Chi cuc thue quan ly';
COMMENT ON COLUMN user_profile.representative IS 'Nguoi dai dien';
COMMENT ON COLUMN user_profile.founding_date IS 'Ngay thanh lap cong ty';
COMMENT ON COLUMN user_profile.business_type IS 'Loai hinh doanh nghiep';
COMMENT ON COLUMN user_profile.province_code IS 'Ma tinh, thanh pho';
COMMENT ON COLUMN user_profile.district_code IS 'Ma quan, huyen';
COMMENT ON COLUMN user_profile.precinct_code IS 'Ma xa, phuong';
COMMENT ON COLUMN user_profile.street_block IS 'Ma duong';
COMMENT ON COLUMN user_profile.create_at IS 'Thoi gian tao';
COMMENT ON COLUMN user_profile.update_at IS 'Thoi gian cap nhat';
COMMENT ON COLUMN user_profile.user_id IS 'ID nguoi dung keycloak';
COMMENT ON COLUMN user_profile.create_by IS 'Nguoi tao';
COMMENT ON COLUMN user_profile.update_by IS 'Nguoi cap nhat';
COMMENT ON COLUMN user_profile.trust_status IS 'Trang thai trust';

-- table option_set
CREATE TABLE option_set
(
    id          VARCHAR(36) PRIMARY KEY NOT NULL,
    code        VARCHAR(50) NOT NULL,
    name        VARCHAR(100) NOT NULL,
    status      INTEGER NOT NULL,
    description VARCHAR(100) NOT NULL,
    create_at   TIMESTAMP DEFAULT CURRENT_TIMESTAMP NULL,
    update_at   TIMESTAMP NULL,
    create_by   VARCHAR(20) NULL,
    update_by   VARCHAR(20) NULL
);
COMMENT ON TABLE option_set IS 'Bang luu thong tin cac option set';
COMMENT ON COLUMN option_set.id IS 'ID cua bang';
COMMENT ON COLUMN option_set.code IS 'Ma cua option set';
COMMENT ON COLUMN option_set.name IS 'Ten cua option set';
COMMENT ON COLUMN option_set.status IS 'Trang thai cua option set';
COMMENT ON COLUMN option_set.description IS 'Mo ta cua option set';
COMMENT ON COLUMN option_set.create_at IS 'Thoi gian tao';
COMMENT ON COLUMN option_set.update_at IS 'Thoi gian cap nhat';
COMMENT ON COLUMN option_set.create_by IS 'Nguoi tao';
COMMENT ON COLUMN option_set.update_by IS 'Nguoi cap nhat';

-- table option_set_value
CREATE TABLE option_set_value
(
    id            VARCHAR(36) PRIMARY KEY NOT NULL,
    option_set_id VARCHAR(36) NOT NULL,
    value         VARCHAR(200) NOT NULL,
    status        INTEGER NOT NULL,
    description   VARCHAR(100) NOT NULL,
    CONSTRAINT fk_option_set FOREIGN KEY (option_set_id) REFERENCES option_set(id)
);
COMMENT ON TABLE option_set_value IS 'Bang luu gia tri cua option set';
COMMENT ON COLUMN option_set_value.id IS 'ID cua bang';
COMMENT ON COLUMN option_set_value.option_set_id IS 'ID cua option set';
COMMENT ON COLUMN option_set_value.value IS 'Gia tri cua option set';
COMMENT ON COLUMN option_set_value.status IS 'Trang thai cua gia tri option set';
COMMENT ON COLUMN option_set_value.description IS 'Mo ta gia tri option set';

