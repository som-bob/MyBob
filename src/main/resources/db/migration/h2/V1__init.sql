-- 초기 테이블 생성 SQL 스크립트
CREATE SCHEMA IF NOT EXISTS mybob;

-- 사용자 테이블
CREATE TABLE IF NOT EXISTS bob_user (
    user_id INTEGER NOT NULL AUTO_INCREMENT,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    nick_name VARCHAR(255) NOT NULL,
    authority VARCHAR(255) DEFAULT 'ROLE_USER',
    last_login_date DATETIME NULL,
    reg_date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    mod_date DATETIME,
    PRIMARY KEY (user_id)
);

-- 사용자 리프레시 토큰 테이블
CREATE TABLE IF NOT EXISTS bob_user_refresh_token (
    token_id BIGINT NOT NULL AUTO_INCREMENT,
    expiry_date DATETIME,
    refresh_token VARCHAR(255),
    user_id BIGINT NOT NULL,
    PRIMARY KEY (token_id)
);

-- 냉장고 테이블
CREATE TABLE IF NOT EXISTS bob_refrigerator (
    refrigerator_id INTEGER NOT NULL AUTO_INCREMENT,
    nickname VARCHAR(100),
    user_id INTEGER NOT NULL,
    reg_date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    mod_date DATETIME,
    PRIMARY KEY (refrigerator_id),
    CONSTRAINT FK_refrigerator_user FOREIGN KEY (user_id)
    REFERENCES bob_user (user_id) ON DELETE CASCADE
);

-- 재료 테이블
CREATE TABLE IF NOT EXISTS bob_ingredients (
    ingredient_id INTEGER NOT NULL AUTO_INCREMENT,
    ingredient_name VARCHAR(100) NOT NULL,
    ingredient_description VARCHAR(1000),
    storage_days SMALLINT,
    storage_method VARCHAR(100),
    icon_url VARCHAR(255),
    image_url VARCHAR(255),
    reg_id varchar(100) NOT NULL,
    reg_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    mod_id varchar(100),
    mod_date DATETIME,
    PRIMARY KEY (ingredient_id)
);

-- 냉장고 재료 매핑 테이블
CREATE TABLE IF NOT EXISTS bob_refrigerator_ingredient (
    refrigerator_ingredient_id INTEGER NOT NULL AUTO_INCREMENT,
    refrigerator_id INTEGER NOT NULL,
    ingredient_id INTEGER NOT NULL,
    date_added DATE,
    reg_date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    mod_date DATETIME,
    PRIMARY KEY (refrigerator_ingredient_id),
    CONSTRAINT FK_refrigerator_ingredient_refrigerator FOREIGN KEY (refrigerator_id)
    REFERENCES bob_refrigerator (refrigerator_id) ON DELETE CASCADE,
    CONSTRAINT FK_refrigerator_ingredient_ingredient FOREIGN KEY (ingredient_id)
    REFERENCES bob_ingredients (ingredient_id) ON DELETE CASCADE
);

-- bob_file definition
CREATE TABLE IF NOT EXISTS bob_file (
	file_id INTEGER NOT NULL AUTO_INCREMENT,
	file_url varchar(255) NOT NULL,
	file_name varchar(255) NULL,
	file_size BIGINT NULL,
	content_type varchar(255) NULL,
	reg_id varchar(100) NOT NULL,
	reg_date datetime NULL,
	PRIMARY KEY (file_id)
)
COMMENT='S3 파일 관련 테이블';

-- 레시피 테이블 (TODO image_url 삭제)
CREATE TABLE IF NOT EXISTS bob_recipe (
    recipe_id INTEGER NOT NULL AUTO_INCREMENT,
    recipe_name VARCHAR(200) NOT NULL,
    recipe_description VARCHAR(1000),
    cooking_time SMALLINT,
    servings VARCHAR(50),
    difficulty VARCHAR(20),
    source VARCHAR(255),
    file_id INTEGER,
    image_url VARCHAR(255),
    reg_id varchar(100) NOT NULL,
    reg_date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    mod_id varchar(100),
    mod_date DATETIME,
    PRIMARY KEY (recipe_id)
);

-- 레시피 재료 테이블
CREATE TABLE IF NOT EXISTS bob_recipe_ingredients (
    detail_ingredient_id INTEGER NOT NULL AUTO_INCREMENT,
    recipe_id INTEGER NOT NULL,
    ingredient_id INTEGER NOT NULL,
    amount VARCHAR(100),
    reg_id varchar(100) NOT NULL,
    reg_date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    mod_id varchar(100),
    mod_date DATETIME,
    PRIMARY KEY (detail_ingredient_id),
    CONSTRAINT FK_recipe_ingredients_recipe FOREIGN KEY (recipe_id)
    REFERENCES bob_recipe (recipe_id),
    CONSTRAINT FK_recipe_ingredients_ingredient FOREIGN KEY (ingredient_id)
    REFERENCES bob_ingredients (ingredient_id)
);

-- 레시피 상세 테이블 (TODO image_url 삭제)
CREATE TABLE IF NOT EXISTS bob_recipe_detail (
    recipe_detail_id INTEGER NOT NULL AUTO_INCREMENT,
    recipe_id INTEGER NOT NULL,
    recipe_order INTEGER NOT NULL,
    recipe_detail_text VARCHAR(3000),
    file_id INTEGER,
    image_url VARCHAR(255),
    reg_id varchar(100) NOT NULL,
    reg_date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    mod_id varchar(100),
    mod_date DATETIME,
    PRIMARY KEY (recipe_detail_id),
    CONSTRAINT FK_recipe_detail_recipe FOREIGN KEY (recipe_id)
    REFERENCES bob_recipe (recipe_id) ON DELETE CASCADE
);

-- 게시판 테이블
CREATE TABLE IF NOT EXISTS bob_board (
    board_id BIGINT NOT NULL AUTO_INCREMENT,
    board_title VARCHAR(255),
    board_content VARCHAR(255),
    is_delete BOOLEAN NOT NULL DEFAULT FALSE,
    reg_id varchar(100) NOT NULL,
    reg_date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    mod_date DATETIME,
    PRIMARY KEY (board_id)
);

-- 게시판 댓글 테이블
CREATE TABLE IF NOT EXISTS bob_board_comment (
    comment_id BIGINT NOT NULL AUTO_INCREMENT,
    board_id BIGINT NOT NULL,
    parent_id BIGINT NULL,
    comment_content VARCHAR(1000) NOT NULL,
    is_delete BOOLEAN NOT NULL DEFAULT FALSE,
    reg_id varchar(100) NOT NULL,
    reg_date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    mod_date DATETIME,
    PRIMARY KEY (comment_id),
    CONSTRAINT FK_board_comment_board FOREIGN KEY (board_id)
    REFERENCES bob_board (board_id),
    CONSTRAINT FK_board_comment_parent FOREIGN KEY (parent_id)
    REFERENCES bob_board_comment (comment_id)
);
