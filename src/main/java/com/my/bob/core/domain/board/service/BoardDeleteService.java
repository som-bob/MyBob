package com.my.bob.core.domain.board.service;

public interface BoardDeleteService {

    void deleteBoard(long boardId, String requestUser);
    void deleteComment(long commentId, String requestUser);

}