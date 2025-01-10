package com.my.bob.core.domain.board.repository;

import com.my.bob.core.domain.board.dto.request.BoardSearchDto;
import com.my.bob.core.domain.board.entity.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BoardQueryRepository {

    Page<Board> getBoardList(BoardSearchDto dto, Pageable pageable);

}
