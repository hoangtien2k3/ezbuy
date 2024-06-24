-- Quản lý người dùng

-- Chứa thông tin về trạng thái của người dùng.
CREATE TYPE user_status AS ENUM ('active', 'inactive', 'banned');

-- Chứa thông tin cơ bản của người dùng.
CREATE TABLE users
(
    user_id         SERIAL PRIMARY KEY,
    username        VARCHAR(50) UNIQUE  NOT NULL,
    email           VARCHAR(100) UNIQUE NOT NULL,
    password_hash   TEXT                NOT NULL,
    first_name      VARCHAR(50),
    last_name       VARCHAR(50),
    phone_number    VARCHAR(15),
    gender          VARCHAR(10) CHECK ( gender IN ('male', 'female', 'other')),
    date_of_birth   DATE,
    profile_picture TEXT,
    status          user_status         NOT NULL DEFAULT 'active',
    postal_code     VARCHAR(20)         NOT NULL,
    country         VARCHAR(100)        NOT NULL,
    created_at      TIMESTAMP                    DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMP                    DEFAULT CURRENT_TIMESTAMP
);

-- Chứa thông tin về các vai trò người dùng.
CREATE TABLE roles
(
    role_id   SERIAL PRIMARY KEY,
    role_name VARCHAR(50) UNIQUE NOT NULL
);

-- Liên kết giữa người dùng và vai trò của họ.
CREATE TABLE user_roles
(
    user_id INT,
    role_id INT,
    PRIMARY KEY (user_id, role_id),
    FOREIGN KEY (user_id) REFERENCES users (user_id) ON DELETE CASCADE,
    FOREIGN KEY (role_id) REFERENCES roles (role_id) ON DELETE CASCADE
);

-- Chứa thông tin địa chỉ của người dùng.
CREATE TABLE addresses
(
    address_id    SERIAL PRIMARY KEY,
    user_id       INT,
    address_line1 VARCHAR(255) NOT NULL,
    address_line2 VARCHAR(255),
    city          VARCHAR(100) NOT NULL,
    state         VARCHAR(100) NOT NULL,
    postal_code   VARCHAR(20)  NOT NULL,
    country       VARCHAR(100) NOT NULL,
    created_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users (user_id) ON DELETE CASCADE
);

-- Chứa thông tin về lịch sử hoạt động của người dùng.
CREATE TABLE user_activity
(
    activity_id        SERIAL PRIMARY KEY,
    user_id            INT,
    activity_type      VARCHAR(50) NOT NULL,
    activity_timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    details            TEXT,
    FOREIGN KEY (user_id) REFERENCES users (user_id) ON DELETE CASCADE
);

-- Chứa thông tin về lịch sử đăng nhập của người dùng.
CREATE TABLE login_history
(
    login_id        SERIAL PRIMARY KEY,
    user_id         INT,
    login_timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    ip_address      VARCHAR(45),
    user_agent      TEXT,
    FOREIGN KEY (user_id) REFERENCES users (user_id) ON DELETE CASCADE
);

-- Chứa các thiết lập tài khoản của người dùng.
CREATE TABLE user_settings
(
    user_id       INT,
    setting_key   VARCHAR(50) NOT NULL,
    setting_value TEXT,
    PRIMARY KEY (user_id, setting_key),
    FOREIGN KEY (user_id) REFERENCES users (user_id) ON DELETE CASCADE
);

-- tạo index cho bảng
-- CREATE INDEX idx_users_email ON users(email);
-- CREATE INDEX idx_user_roles_user_id ON user_roles(user_id);
-- CREATE INDEX idx_addresses_user_id ON addresses(user_id);
-- CREATE INDEX idx_user_activity_user_id ON user_activity(user_id);
-- CREATE INDEX idx_login_history_user_id ON login_history(user_id);
-- CREATE INDEX idx_user_settings_user_id ON user_settings(user_id);
-- CREATE INDEX idx_user_sessions_user_id ON user_sessions(user_id);
-- CREATE INDEX idx_user_preferences_user_id ON user_preferences(user_id);
