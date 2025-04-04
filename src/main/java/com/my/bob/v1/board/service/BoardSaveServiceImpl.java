package com.my.bob.v1.board.service;

import com.my.bob.core.domain.board.dto.request.BoardCommentCreateDto;
import com.my.bob.core.domain.board.dto.request.BoardCommentUpdateDto;
import com.my.bob.core.domain.board.dto.request.BoardCreateDto;
import com.my.bob.core.domain.board.dto.request.BoardUpdateDto;
import com.my.bob.core.domain.board.entity.Board;
import com.my.bob.core.domain.board.entity.BoardComment;
import com.my.bob.core.domain.board.service.BoardCommentService;
import com.my.bob.core.domain.board.service.BoardSaveService;
import com.my.bob.core.domain.board.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.my.bob.core.constants.ErrorMessage.DO_NOT_HAVE_PERMISSION;
import static com.my.bob.core.constants.ErrorMessage.INVALID_REQUEST;

@Service
@RequiredArgsConstructor
public class BoardSaveServiceImpl implements BoardSaveService {

    private final BoardService boardService;
    private final BoardCommentService boardCommentService;

    @Transactional
    public long saveNewBoard(BoardCreateDto dto){
        String title = dto.getTitle();
        String content = dto.getContent();
        if(content.length() > 1000) {   // 이거 Email 같은 어노테이션으로 교체
            throw new IllegalStateException("게시글은 1000자를 조과할 수 없습니다.");
        }

        Board board = new Board(title, content);
        boardService.save(board);

        return board.getBoardId();
    }

    @Transactional
    public void updateBoard(long boardId, String requestUser, BoardUpdateDto dto) {
        Board board = boardService.getById(boardId);

        if(! board.isRegistrant(requestUser)) {
            throw new IllegalStateException(DO_NOT_HAVE_PERMISSION);
        }
        if(board.isDelete()) {
            throw new IllegalStateException(INVALID_REQUEST);
        }

        board.updateBoard(dto.getTitle(), dto.getContent());
        boardService.save(board);
    }

    @Transactional
    public void saveNewComment(long bordId, BoardCommentCreateDto dto) {
        Board board = boardService.getById(bordId);

        Long parentCommentId = dto.getParentCommentId();
        BoardComment parentComment = null;
        if(parentCommentId != null) {
            parentComment = boardCommentService.getById(parentCommentId);
        }

        BoardComment boardComment = new BoardComment(board, parentComment, dto.getComment());
        boardCommentService.save(boardComment);
    }

    @Transactional
    public void updateComment(long commentId, String requestUser, BoardCommentUpdateDto dto) {
        BoardComment comment = boardCommentService.getById(commentId);
        String regId = comment.getRegId();

        if(! requestUser.equals(regId)) {
            throw new IllegalStateException(DO_NOT_HAVE_PERMISSION);
        }
        if(comment.isDelete()) {
            throw new IllegalStateException(INVALID_REQUEST);
        }

        comment.updateComment(dto.getComment());
        boardCommentService.save(comment);
    }
}
