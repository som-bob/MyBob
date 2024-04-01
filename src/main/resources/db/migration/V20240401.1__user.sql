-- init user table
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