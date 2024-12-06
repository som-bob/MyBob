package com.my.bob.core.domain.board.service;

import com.my.bob.core.domain.board.entity.BoardComment;

public interface BoardCommentService {

    void save(BoardComment boardComment);
    BoardComment getById(long commentId);
}
