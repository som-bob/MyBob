-- bob_user definition
CREATE TABLE IF NOT EXISTS `bob_user` (
  `user_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '유저 ID (PF)',
  `email` varchar(100) NOT NULL COMMENT '유저 이메일 (로그인용)',
  `password` varchar(100) NOT NULL COMMENT '유저 비밀번호 (암호화된)',
  `nick_name` varchar(20) NOT NULL COMMENT '유저 닉네임',
  `authority` varchar(100) NOT NULL DEFAULT 'ROLE_MEMBER' COMMENT '유저 권한',
  `last_login_date` datetime(6) DEFAULT NULL COMMENT '마지막 로그인 일시',
  `reg_date` datetime(6) DEFAULT NULL COMMENT '가입일',
  `mod_date` datetime(6) DEFAULT NULL COMMENT '수정일',
  PRIMARY KEY (`user_id`)
) COMMENT='일반 유저';

-- bob_user_refresh_token definition
CREATE TABLE IF NOT EXISTS `bob_user_refresh_token` (
  `token_id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `refresh_token` varchar(255) NOT NULL,
  `expiry_date` datetime(6) NOT NULL,
  PRIMARY KEY (`token_id`),
  UNIQUE KEY `refresh_token` (`refresh_token`)
);

-- bob_board definition
CREATE TABLE IF NOT EXISTS `bob_board` (
  `board_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '게시판 id (PK)',
  `board_title` varchar(50) NOT NULL COMMENT '게시판 제목',
  `board_content` varchar(1000) NOT NULL COMMENT '게시판 글',
  `is_delete` tinyint(4) NOT NULL DEFAULT 0 COMMENT '삭제 여부',
  `reg_id` varchar(100) NOT NULL COMMENT '글쓴이 이메일',
  `reg_date` datetime NOT NULL COMMENT '등록일',
  `mod_date` datetime DEFAULT NULL COMMENT '수정일',
  PRIMARY KEY (`board_id`)
) COMMENT='일반 게시판 테이블';

-- bob_board_comment definition
CREATE TABLE IF NOT EXISTS `bob_board_comment` (
  `comment_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '댓글 id (PK)',
  `board_id` int(11) NOT NULL COMMENT '게시판 id (FK)',
  `parent_id` int(11) DEFAULT NULL COMMENT '부모 댓글 id (NULL이면 일반 댓글)',
  `comment_content` varchar(1000) NOT NULL COMMENT '댓글 내용',
  `is_delete` tinyint(4) NOT NULL DEFAULT 0 COMMENT '삭제 여부',
  `reg_id` varchar(100) NOT NULL COMMENT '댓글 작성자 이메일',
  `reg_date` datetime NOT NULL COMMENT '댓글 작성일',
  `mod_date` datetime DEFAULT NULL COMMENT '댓글 수정일',
  PRIMARY KEY (`comment_id`),
  KEY `bob_comment_fk_board_id` (`board_id`),
  KEY `bob_comment_fk_parent_id` (`parent_id`),
  CONSTRAINT `bob_comment_fk_board_id` FOREIGN KEY (`board_id`) REFERENCES `bob_board` (`board_id`) ON DELETE CASCADE,
  CONSTRAINT `bob_comment_fk_parent_id` FOREIGN KEY (`parent_id`) REFERENCES `bob_board_comment` (`comment_id`) ON DELETE CASCADE
) COMMENT='댓글 및 대댓글 테이블';

-- bob_ingredients definition
CREATE TABLE IF NOT EXISTS `bob_ingredients` (
  `ingredient_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '재료 ID',
  `ingredient_name` varchar(100) DEFAULT NULL COMMENT '재료명',
  `ingredient_description` varchar(1000) DEFAULT NULL COMMENT '재료 설명',
  `image_url` varchar(255) DEFAULT NULL COMMENT '재료 이미지 URL',
  `icon_url` varchar(255) DEFAULT NULL COMMENT '재료 아이콘 URL',
  `storage_method` varchar(100) DEFAULT NULL COMMENT '보관 방법',
  `storage_days` smallint(6) DEFAULT NULL COMMENT '보관 가능 일수 (구매일로부터)',
  `reg_id` varchar(100) NOT NULL COMMENT '글쓴이 이메일',
  `reg_date` datetime DEFAULT NULL COMMENT '등록 날짜',
  `mod_id` varchar(100) DEFAULT NULL COMMENT '수정자 이메일',
  `mod_date` datetime DEFAULT NULL COMMENT '수정 날짜',
  PRIMARY KEY (`ingredient_id`)
) COMMENT='재료 테이블';

-- bob_recipe definition
CREATE TABLE IF NOT EXISTS `bob_recipe` (
  `recipe_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '레시피 ID',
  `recipe_name` varchar(200) NOT NULL COMMENT '레시피명',
  `recipe_description` varchar(1000) DEFAULT NULL COMMENT '레시피 설명',
  `servings` varchar(50) DEFAULT NULL COMMENT '몇인분인지 정보',
  `difficulty` varchar(20) DEFAULT NULL COMMENT '난이도',
  `cooking_time` smallint(6) DEFAULT NULL COMMENT '소요시간',
  `source` varchar(255) DEFAULT NULL COMMENT '출처 URL 또는 출처 정보',
  `image_url` varchar(255) DEFAULT NULL COMMENT '대표 이미지 URL',
  `reg_id` varchar(100) NOT NULL COMMENT '글쓴이 이메일',
  `reg_date` datetime DEFAULT NULL COMMENT '등록 날짜',
  `mod_id` varchar(100) DEFAULT NULL COMMENT '수정자 이메일',
  `mod_date` datetime DEFAULT NULL COMMENT '수정 날짜',
  PRIMARY KEY (`recipe_id`)
) COMMENT='레시피 테이블';

-- bob_recipe_detail definition
CREATE TABLE IF NOT EXISTS `bob_recipe_detail` (
  `recipe_detail_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '레시피 상세 ID',
  `recipe_id` int(11) NOT NULL COMMENT '레시피 ID (bob_recipe와 FK)',
  `image_url` varchar(255) DEFAULT NULL COMMENT '레시피 상세 이미지 URL',
  `recipe_order` int(11) NOT NULL COMMENT '레시피 순서',
  `recipe_detail_text` varchar(3000) DEFAULT NULL COMMENT '레시피 상세 텍스트 (최대 3000자)',
  `reg_id` varchar(100) NOT NULL COMMENT '글쓴이 이메일',
  `reg_date` datetime DEFAULT NULL COMMENT '등록 날짜',
  `mod_id` varchar(100) DEFAULT NULL COMMENT '수정자 이메일',
  `mod_date` datetime DEFAULT NULL COMMENT '수정 날짜',
  PRIMARY KEY (`recipe_detail_id`),
  KEY `bob_recipe_detail_bob_recipe_FK` (`recipe_id`),
  CONSTRAINT `bob_recipe_detail_bob_recipe_FK` FOREIGN KEY (`recipe_id`) REFERENCES `bob_recipe` (`recipe_id`) ON DELETE CASCADE ON UPDATE CASCADE
) COMMENT='레시피 상세 테이블';

-- bob_recipe_ingredients definition
CREATE TABLE IF NOT EXISTS `bob_recipe_ingredients` (
  `detail_ingredient_id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'pk',
  `recipe_id` int(11) NOT NULL COMMENT '레시피 ID (FK)',
  `ingredient_id` int(11) NOT NULL COMMENT '재료 ID (FK)',
  `ingredient_detail_name` varchar(200) DEFAULT NULL COMMENT '재료 자세한 이름',
  `amount` varchar(100) DEFAULT NULL COMMENT '양 텍스트 (ex: 1/2T, 3T, 적당량 등)',
  `reg_id` varchar(100) NOT NULL COMMENT '글쓴이 이메일',
  `reg_date` datetime DEFAULT NULL COMMENT '등록 날짜',
  `mod_id` varchar(100) DEFAULT NULL COMMENT '수정자 이메일',
  `mod_date` datetime DEFAULT NULL COMMENT '수정 날짜',
  PRIMARY KEY (`detail_ingredient_id`),
  KEY `bob_recipe_ingredients_bob_recipe_FK` (`recipe_id`),
  KEY `bob_recipe_ingredients_bob_ingredients_FK` (`ingredient_id`),
  CONSTRAINT `bob_recipe_ingredients_bob_ingredients_FK` FOREIGN KEY (`ingredient_id`) REFERENCES `bob_ingredients` (`ingredient_id`),
  CONSTRAINT `bob_recipe_ingredients_bob_recipe_FK` FOREIGN KEY (`recipe_id`) REFERENCES `bob_recipe` (`recipe_id`)
) COMMENT='레시피 재료 상세';

-- bob_refrigerator definition
CREATE TABLE IF NOT EXISTS `bob_refrigerator` (
  `refrigerator_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '냉장고 ID',
  `nickname` varchar(100) DEFAULT NULL COMMENT '냉장고 애칭',
  `user_id` int(11) NOT NULL COMMENT 'bob_user 테이블의 유저 ID (1:1 매핑)',
  `reg_date` datetime DEFAULT NULL COMMENT '등록 날짜',
  `mod_date` datetime DEFAULT NULL COMMENT '수정 날짜',
  PRIMARY KEY (`refrigerator_id`),
  KEY `fk_bob_user` (`user_id`),
  CONSTRAINT `fk_bob_user` FOREIGN KEY (`user_id`) REFERENCES `bob_user` (`user_id`) ON DELETE CASCADE ON UPDATE CASCADE
) COMMENT='나의 냉장고 테이블';

-- bob_refrigerator_ingredient definition
CREATE TABLE IF NOT EXISTS `bob_refrigerator_ingredient` (
  `refrigerator_ingredient_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '냉장고 재료 ID',
  `refrigerator_id` int(11) NOT NULL COMMENT '냉장고 ID(bob_refreigerator와 1:N매핑)',
  `ingredient_id` int(11) NOT NULL COMMENT '재료 ID (bob_ingredients와 FK)',
  `date_added` date DEFAULT NULL COMMENT '재료가 냉장고에 들어온 날짜',
  `reg_date` datetime DEFAULT NULL COMMENT '등록 날짜',
  `mod_date` datetime DEFAULT NULL COMMENT '수정 날짜',
  PRIMARY KEY (`refrigerator_ingredient_id`),
  KEY `bob_refrigerator_ingredient_bob_refrigerator_FK` (`refrigerator_id`),
  KEY `bob_refrigerator_ingredient_bob_ingredients_FK` (`ingredient_id`),
  CONSTRAINT `bob_refrigerator_ingredient_bob_ingredients_FK` FOREIGN KEY (`ingredient_id`) REFERENCES `bob_ingredients` (`ingredient_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `bob_refrigerator_ingredient_bob_refrigerator_FK` FOREIGN KEY (`refrigerator_id`) REFERENCES `bob_refrigerator` (`refrigerator_id`) ON DELETE CASCADE ON UPDATE CASCADE
) COMMENT='냉장고 속 재료 테이블';
