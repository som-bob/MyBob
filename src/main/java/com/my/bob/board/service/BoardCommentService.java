package com.my.bob.board.service;

import com.my.bob.board.entity.BoardComment;
import com.my.bob.board.repository.BoardCommentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
}
