package com.my.bob.board.service;

import com.my.bob.board.entity.Board;
import com.my.bob.board.entity.BoardComment;
import com.my.bob.board.repository.BoardCommentRepository;
import com.my.bob.constants.ErrorMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardCommentService {

    private final BoardCommentRepository boardCommentRepository;

    @Transactional
    public void save(BoardComment boardComment) {
        boardCommentRepository.save(boardComment);
    }

    public BoardComment getById(long commentId) {
        return boardCommentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalStateException(ErrorMessage.INVALID_REQUEST));
    }

    public List<BoardComment> getRootCommentBy(Board board) {
        return boardCommentRepository.findAllByBoardAndParentCommentIsNullOrderByRegDateAsc(board);
    }
}
