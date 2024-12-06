package com.my.bob.core.domain.board.service;

import com.my.bob.core.domain.board.dto.BoardDto;
import com.my.bob.core.domain.board.dto.BoardTitleDto;
import com.my.bob.core.domain.board.entity.Board;

public interface BoardMapperService {

    BoardDto convertBoardDto(long boardId);

    BoardTitleDto convertTitleDto(Board board);
}
