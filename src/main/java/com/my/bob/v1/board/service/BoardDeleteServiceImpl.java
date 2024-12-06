package com.my.bob.v1.board.service;

import com.my.bob.core.domain.board.entity.Board;
import com.my.bob.core.domain.board.entity.BoardComment;
import com.my.bob.core.domain.board.service.BoardDeleteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.my.bob.core.constants.ErrorMessage.DO_NOT_HAVE_PERMISSION;

@Slf4j
@Service
@RequiredArgsConstructor
public class BoardDeleteServiceImpl implements BoardDeleteService {

    private final BoardServiceImpl boardServiceImpl;
    private final BoardCommentServiceImpl boardCommentServiceImpl;


    @Transactional
    public void deleteBoard(long boardId, String requestUser){
        Board board = boardServiceImpl.getById(boardId);
        if(! board.isRegistrant(requestUser)) {
            throw new IllegalStateException(DO_NOT_HAVE_PERMISSION);
        }

        board.deleteBoard();
        boardServiceImpl.save(board);
    }

    @Transactional
    public void deleteComment(long commentId, String requestUser){
        BoardComment comment = boardCommentServiceImpl.getById(commentId);
        if(! comment.isRegistrant(requestUser)) {
            throw new IllegalStateException(DO_NOT_HAVE_PERMISSION);
        }

        comment.deleteComment();
        boardCommentServiceImpl.save(comment);
    }
}
