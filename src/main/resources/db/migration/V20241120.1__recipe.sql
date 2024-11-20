-- 재료
CREATE TABLE IF NOT EXISTS bob_ingredients
(
    ingredient_id   INT AUTO_INCREMENT PRIMARY KEY COMMENT '재료 ID',
    ingredient_name VARCHAR(100) NOT NULL COMMENT '재료 이름',
    ingredient_description     VARCHAR(1000) COMMENT '재료 설명',  -- TEXT -> VARCHAR(500)
    image_url       VARCHAR(255) COMMENT '재료 이미지 URL',
    icon_url        VARCHAR(255) COMMENT '재료 아이콘 URL',
    storage_method  VARCHAR(100) COMMENT '보관 방법',
    storage_days    SMALLINT COMMENT '보관 가능 일수 (구매일로부터)',
    reg_id varchar(100) NOT NULL COMMENT '글쓴이 이메일',
    reg_date DATETIME COMMENT '등록 날짜',
    mod_id varchar(100) DEFAULT NULL COMMENT '수정자 이메일',
    mod_date DATETIME COMMENT '수정 날짜'
) COMMENT ='재료 테이블';
    

-- 나의 냉장고
CREATE TABLE IF NOT EXISTS bob_refrigerator (
    refrigerator_id INT AUTO_INCREMENT PRIMARY KEY COMMENT '냉장고 ID',
    nickname VARCHAR(100) COMMENT '냉장고 애칭',
    user_id INT NOT NULL COMMENT 'bob_user 테이블의 유저 ID (1:1 매핑)',
    reg_date DATETIME COMMENT '등록 날짜',
    mod_date DATETIME COMMENT '수정 날짜',
    CONSTRAINT `fk_bob_user` FOREIGN KEY (`user_id`) REFERENCES `bob_user` (`user_id`) ON DELETE CASCADE ON UPDATE cascade
) COMMENT='나의 냉장고 테이블';
    

-- 냉장고 재료 (냉장고 속에 들어있는 재료 테이블)
CREATE TABLE bob_refrigerator_ingredient (
    refrigerator_ingredient_id INT AUTO_INCREMENT PRIMARY KEY COMMENT '냉장고 재료 ID',
    refrigerator_id INT NOT NULL COMMENT '냉장고 ID(bob_refreigerator와 1:N매핑)',
    ingredient_id INT NOT NULL COMMENT '재료 ID (bob_ingredients와 FK)',
    date_added DATE COMMENT '재료가 냉장고에 들어온 날짜', -- 기본값 제거
    reg_date DATETIME COMMENT '등록 날짜',
    mod_date DATETIME COMMENT '수정 날짜',
    CONSTRAINT bob_refrigerator_ingredient_bob_refrigerator_FK FOREIGN KEY (refrigerator_id) REFERENCES bob_refrigerator(refrigerator_id) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT bob_refrigerator_ingredient_bob_ingredients_FK FOREIGN KEY (ingredient_id) REFERENCES bob_ingredients(ingredient_id) ON DELETE CASCADE ON UPDATE CASCADE
) COMMENT='냉장고 속 재료 테이블';



-- 레시피
CREATE TABLE IF NOT EXISTS bob_recipe (
    recipe_id INT AUTO_INCREMENT PRIMARY KEY COMMENT '레시피 ID',
    recipe_name VARCHAR(200) NOT NULL COMMENT '레시피명',
    recipe_description VARCHAR(1000) COMMENT '레시피 설명',
    source VARCHAR(255) COMMENT '출처 URL 또는 출처 정보',
    image_url VARCHAR(255) COMMENT '대표 이미지 URL',
    reg_id varchar(100) NOT NULL COMMENT '글쓴이 이메일',
    reg_date DATETIME COMMENT '등록 날짜',
    mod_id varchar(100) DEFAULT NULL COMMENT '수정자 이메일',
    mod_date DATETIME COMMENT '수정 날짜'
) COMMENT='레시피 테이블';


-- 레시피 상세
CREATE TABLE IF NOT EXISTS bob_recipe_detail (
  `recipe_detail_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '레시피 상세 ID',
  `recipe_id` int(11) NOT NULL COMMENT '레시피 ID (bob_recipe와 FK)',
  `image_url` varchar(255) DEFAULT NULL COMMENT '레시피 상세 이미지 URL',
  `recipe_order` int(11) NOT NULL COMMENT '레시피 순서',
  `recipe_detail_text` varchar(3000) DEFAULT NULL COMMENT '레시피 상세 텍스트 (최대 3000자)',
  reg_id varchar(100) NOT NULL COMMENT '글쓴이 이메일',
  reg_date DATETIME COMMENT '등록 날짜',
  mod_id varchar(100) DEFAULT NULL COMMENT '수정자 이메일',
  mod_date DATETIME COMMENT '수정 날짜',
  PRIMARY KEY (`recipe_detail_id`),
  KEY `bob_recipe_detail_bob_recipe_FK` (`recipe_id`),
  CONSTRAINT `bob_recipe_detail_bob_recipe_FK` FOREIGN KEY (`recipe_id`) REFERENCES `bob_recipe` (`recipe_id`) ON DELETE CASCADE ON UPDATE CASCADE
) COMMENT='레시피 상세 테이블';



-- 레시피 상세 재료
CREATE TABLE IF NOT EXISTS bob_recipe_detail_ingredient (
    detail_ingredient_id INT AUTO_INCREMENT PRIMARY KEY COMMENT '상세 재료 ID',
    recipe_detail_id INT NOT NULL COMMENT '레시피 상세 ID (bob_recipe_detail와 FK)',
    ingredient_id INT NOT NULL COMMENT '재료 ID (bob_ingredients와 FK)',
    amount VARCHAR(100) COMMENT '양 텍스트 (ex: 1/2T, 3T, 적당량 등)',
    reg_id varchar(100) NOT NULL COMMENT '글쓴이 이메일',
    reg_date DATETIME COMMENT '등록 날짜',
    mod_id varchar(100) DEFAULT NULL COMMENT '수정자 이메일',
    mod_date DATETIME COMMENT '수정 날짜',
    CONSTRAINT `bob_recipe_detail_ingredient_bob_ingredients_FK` FOREIGN KEY (`ingredient_id`) REFERENCES `bob_ingredients` (`ingredient_id`),
  CONSTRAINT `bob_recipe_detail_ingredient_bob_recipe_detail_FK` FOREIGN KEY (`recipe_detail_id`) REFERENCES `bob_recipe_detail` (`recipe_detail_id`)
) COMMENT='레시피 상세 재료 테이블';
    
