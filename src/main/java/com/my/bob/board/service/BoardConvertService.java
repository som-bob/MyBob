package com.my.bob.board.service;

import com.my.bob.board.dto.BoardCommentDto;
import com.my.bob.board.dto.BoardDto;
import com.my.bob.board.dto.BoardSearchDto;
import com.my.bob.board.dto.BoardTitleDto;
import com.my.bob.board.entity.Board;
import com.my.bob.board.entity.BoardComment;
import com.my.bob.util.DateConvertUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.hibernate.type.descriptor.DateTimeUtils;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardConvertService {

    private final BoardService boardService;

    private final ModelMapper modelMapper;


    public BoardDto convertBoardDto(long boardId) {
        Board board = boardService.getById(boardId);

        BoardDto dto = new BoardDto();
        dto.setBoardId(boardId);
        dto.setTitle(board.getBoardTitle());
        dto.setContent(board.getBoardContent());
        dto.setDelete(board.isDelete());

        List<BoardComment> comments = board.getComments();
        Optional.of(comments)
                .ifPresent(
                        boardComments -> {
                            List<BoardCommentDto> commentDtoList =
                                    boardComments
                                            .stream()
                                            .filter(BoardComment::isRootComment)
                                            .map(this::convertCommentDto)
                                            .toList();
                            dto.setCommentList(commentDtoList);
                        });

        return dto;
    }

    public Page<BoardTitleDto> convertBoardList(BoardSearchDto dto, Pageable pageable) {
        return boardService.getBySearch(dto, pageable).map(this::convertTitleDto);
    }

    /* private */
    private BoardCommentDto convertCommentDto(BoardComment boardComment) {
        long commentId = boardComment.getCommentId();

        BoardCommentDto commentDto = new BoardCommentDto();
        commentDto.setCommentId(commentId);
        commentDto.setContent(boardComment.getCommentContent());
        commentDto.setDelete(boardComment.isDelete());

        // 하위 댓글 TODO
        List<BoardComment> childComment = boardComment.getChildComments();
        childComment.sort((o1, o2) -> (int) (o1.getCommentId() - o2.getCommentId()));
        if (CollectionUtils.isNotEmpty(childComment)) {
            List<BoardCommentDto> dtoList =
                    childComment
                            .stream()
                            .map(this::convertCommentDto)
                            .toList();
            commentDto.setSubComments(dtoList);
        }

        return commentDto;
    }

    private BoardTitleDto convertTitleDto(Board board) {
        if (board.isDelete()) {
            BoardTitleDto dto = new BoardTitleDto();
            dto.setBoardTitle("삭제된 글입니다.");
            return dto;
        }

        BoardTitleDto dto = modelMapper.map(board, BoardTitleDto.class);

        String regDateStr = board.getRegDate().toLocalDate().toString();
        dto.setRegDate(regDateStr);

        return dto;
    }
}
