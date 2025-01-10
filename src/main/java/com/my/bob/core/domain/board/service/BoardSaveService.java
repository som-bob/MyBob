package com.my.bob.core.domain.board.service;

import com.my.bob.core.domain.board.dto.request.BoardCommentCreateDto;
import com.my.bob.core.domain.board.dto.request.BoardCommentUpdateDto;
import com.my.bob.core.domain.board.dto.request.BoardCreateDto;
import com.my.bob.core.domain.board.dto.request.BoardUpdateDto;

public interface BoardSaveService {
    long saveNewBoard(BoardCreateDto dto);

    void updateBoard(long boardId, String requestUser, BoardUpdateDto dto);

    void saveNewComment(long bordId, BoardCommentCreateDto dto);

    void updateComment(long commentId, String requestUser, BoardCommentUpdateDto dto);

}
