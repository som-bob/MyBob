CREATE TABLE if not exists bob_board (
	board_id int(11) auto_increment NOT NULL COMMENT '게시판 id (PK)',
	board_title varchar(50) NOT NULL COMMENT '게시판 제목',
	board_content varchar(1000) NOT NULL COMMENT '게시판 글',
	reg_id int(11) NOT NULL COMMENT '글쓴이',
	reg_date DATETIME NOT NULL COMMENT '등록일',
	mod_date DATETIME NOT NULL COMMENT '수정일',
	CONSTRAINT bob_board_pk PRIMARY KEY (board_id)
) COMMENT='일반 게시판 테이블';

CREATE TABLE if not exists bob_comment (
    comment_id int(11) auto_increment NOT NULL COMMENT '댓글 id (PK)',
    board_id int(11) NOT NULL COMMENT '게시판 id (FK)',
    parent_id int(11) DEFAULT NULL COMMENT '부모 댓글 id (NULL이면 일반 댓글)',
    comment_content varchar(1000) NOT NULL COMMENT '댓글 내용',
    reg_id int(11) NOT NULL COMMENT '댓글 작성자',
    reg_date DATETIME NOT NULL COMMENT '댓글 작성일',
    mod_date DATETIME NOT NULL COMMENT '댓글 수정일',
    CONSTRAINT bob_comment_pk PRIMARY KEY (comment_id),
    CONSTRAINT bob_comment_fk_board_id FOREIGN KEY (board_id) REFERENCES bob_board(board_id) ON DELETE CASCADE,
    CONSTRAINT bob_comment_fk_parent_id FOREIGN KEY (parent_id) REFERENCES bob_comment(comment_id) ON DELETE CASCADE
) COMMENT='댓글 및 대댓글 테이블';