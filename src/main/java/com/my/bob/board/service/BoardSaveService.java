package com.my.bob.board.service;

import com.my.bob.board.dto.BoardCommentCreateDto;
import com.my.bob.board.dto.BoardCreateDto;
import com.my.bob.board.dto.BoardUpdateDto;
import com.my.bob.board.entity.Board;
import com.my.bob.board.entity.BoardComment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.my.bob.constants.ErrorMessage.DO_NOT_HAVE_PERMISSION;

@Service
@RequiredArgsConstructor
public class BoardSaveService {

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
    public void updateBoard(long boardId, String userEmail, BoardUpdateDto dto) {
        Board board = boardService.getById(boardId);
        String regId = board.getRegId();

        if(! userEmail.equals(regId)) {
            throw new IllegalStateException(DO_NOT_HAVE_PERMISSION);
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
}
