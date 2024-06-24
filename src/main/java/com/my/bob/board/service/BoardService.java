package com.my.bob.board.service;

import com.my.bob.board.entity.Board;
import com.my.bob.board.repository.BoardRepository;
import com.my.bob.constants.ErrorMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardService {

    private final BoardRepository boardRepository;

    @Transactional
    public void save(Board board) {
        boardRepository.save(board);
    }

    public Board getById(long boardId) {
        return boardRepository.findById(boardId)
                .orElseThrow(() -> new IllegalStateException(ErrorMessage.NON_EXISTENT_POST));
    }

}
