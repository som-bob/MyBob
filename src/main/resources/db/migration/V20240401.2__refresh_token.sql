CREATE TABLE if not exists bob_user_refresh_token (
    id int(11) AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    refresh_token VARCHAR(255) NOT NULL,
    expiry_date DATETIME(6) NOT NULL,
    UNIQUE (refresh_token)
);
