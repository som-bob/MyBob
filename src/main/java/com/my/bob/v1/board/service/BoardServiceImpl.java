package com.my.bob.v1.board.service;

import com.my.bob.core.constants.ErrorMessage;
import com.my.bob.core.domain.board.dto.request.BoardSearchDto;
import com.my.bob.core.domain.board.entity.Board;
import com.my.bob.core.domain.board.repository.BoardQueryRepository;
import com.my.bob.core.domain.board.repository.BoardRepository;
import com.my.bob.core.domain.board.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardServiceImpl implements BoardService {

    private final BoardRepository boardRepository;
    private final BoardQueryRepository boardQueryRepository;

    @Transactional
    public void save(Board board) {
        boardRepository.save(board);
    }

    public Board getById(long boardId) {
        return boardRepository.findById(boardId)
                .orElseThrow(() -> new IllegalStateException(ErrorMessage.NOT_EXISTENT_POST));
    }

    public Page<Board> getBySearch(BoardSearchDto dto, Pageable pageable) {
        return boardQueryRepository.getBoardList(dto, pageable);
    }
}
