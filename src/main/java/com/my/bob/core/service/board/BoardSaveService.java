package com.my.bob.core.service.board;

import com.my.bob.core.domain.board.dto.BoardCommentCreateDto;
import com.my.bob.core.domain.board.dto.BoardCommentUpdateDto;
import com.my.bob.core.domain.board.dto.BoardCreateDto;
import com.my.bob.core.domain.board.dto.BoardUpdateDto;

public interface BoardSaveService {
    long saveNewBoard(BoardCreateDto dto);

    void updateBoard(long boardId, String requestUser, BoardUpdateDto dto);

    void saveNewComment(long bordId, BoardCommentCreateDto dto);

    void updateComment(long commentId, String requestUser, BoardCommentUpdateDto dto);

}
