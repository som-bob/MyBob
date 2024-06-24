ALTER TABLE bob_board ADD is_delete TINYINT DEFAULT 0 NOT NULL COMMENT '삭제 여부';
ALTER TABLE bob_board CHANGE is_delete is_delete TINYINT DEFAULT 0 NOT NULL COMMENT '삭제 여부' AFTER board_content;

ALTER TABLE bob_board_comment ADD is_delete TINYINT DEFAULT 0 NOT NULL COMMENT '삭제 여부';
ALTER TABLE bob_board_comment CHANGE is_delete is_delete TINYINT DEFAULT 0 NOT NULL COMMENT '삭제 여부' AFTER comment_content;