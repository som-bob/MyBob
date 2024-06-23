ALTER TABLE bob_board MODIFY COLUMN reg_id varchar(100) NOT NULL COMMENT '글쓴이 이메일';
ALTER TABLE bob_comment MODIFY COLUMN reg_id varchar(100) NOT NULL COMMENT '댓글 작성자 이메일';
