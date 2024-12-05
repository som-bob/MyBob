package com.my.bob.core.service.board;

import com.my.bob.core.domain.board.dto.BoardSearchDto;
import com.my.bob.core.domain.board.entity.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BoardService {

    void save(Board board);
    Board getById(long boardId);
    Page<Board> getBySearch(BoardSearchDto condition, Pageable pageable);
}
