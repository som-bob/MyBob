package com.my.bob.core.service.board;

public interface BoardDeleteService {

    void deleteBoard(long boardId, String requestUser);
    void deleteComment(long commentId, String requestUser);

}