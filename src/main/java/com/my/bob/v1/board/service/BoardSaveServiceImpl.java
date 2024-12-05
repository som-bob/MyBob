package com.my.bob.v1.board.service;

import com.my.bob.core.domain.board.dto.BoardCommentCreateDto;
import com.my.bob.core.domain.board.dto.BoardCommentUpdateDto;
import com.my.bob.core.domain.board.dto.BoardCreateDto;
import com.my.bob.core.domain.board.dto.BoardUpdateDto;
import com.my.bob.core.domain.board.entity.Board;
import com.my.bob.core.domain.board.entity.BoardComment;
import com.my.bob.core.service.board.BoardSaveService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.my.bob.core.constants.ErrorMessage.DO_NOT_HAVE_PERMISSION;
import static com.my.bob.core.constants.ErrorMessage.INVALID_REQUEST;

@Service
@RequiredArgsConstructor
public class BoardSaveServiceImpl implements BoardSaveService {

    private final BoardServiceImpl boardServiceImpl;
    private final BoardCommentServiceImpl boardCommentServiceImpl;

    @Transactional
    public long saveNewBoard(BoardCreateDto dto){
        String title = dto.getTitle();
        String content = dto.getContent();
        if(content.length() > 1000) {   // 이거 Email 같은 어노테이션으로 교체
            throw new IllegalStateException("게시글은 1000자를 조과할 수 없습니다.");
        }

        Board board = new Board(title, content);
        boardServiceImpl.save(board);

        return board.getBoardId();
    }

    @Transactional
    public void updateBoard(long boardId, String requestUser, BoardUpdateDto dto) {
        Board board = boardServiceImpl.getById(boardId);

        if(! board.isRegistrant(requestUser)) {
            throw new IllegalStateException(DO_NOT_HAVE_PERMISSION);
        }
        if(board.isDelete()) {
            throw new IllegalStateException(INVALID_REQUEST);
        }

        board.updateBoard(dto.getTitle(), dto.getContent());
        boardServiceImpl.save(board);
    }

    @Transactional
    public void saveNewComment(long bordId, BoardCommentCreateDto dto) {
        Board board = boardServiceImpl.getById(bordId);

        Long parentCommentId = dto.getParentCommentId();
        BoardComment parentComment = null;
        if(parentCommentId != null) {
            parentComment = boardCommentServiceImpl.getById(parentCommentId);
        }

        BoardComment boardComment = new BoardComment(board, parentComment, dto.getComment());
        boardCommentServiceImpl.save(boardComment);
    }

    @Transactional
    public void updateComment(long commentId, String requestUser, BoardCommentUpdateDto dto) {
        BoardComment comment = boardCommentServiceImpl.getById(commentId);
        String regId = comment.getRegId();

        if(! requestUser.equals(regId)) {
            throw new IllegalStateException(DO_NOT_HAVE_PERMISSION);
        }
        if(comment.isDelete()) {
            throw new IllegalStateException(INVALID_REQUEST);
        }

        comment.updateComment(dto.getComment());
        boardCommentServiceImpl.save(comment);
    }
}
