package com.my.bob.v1.board.service;

import com.my.bob.core.domain.board.dto.response.BoardCommentDto;
import com.my.bob.core.domain.board.dto.response.BoardDto;
import com.my.bob.core.domain.board.dto.response.BoardTitleDto;
import com.my.bob.core.domain.board.entity.Board;
import com.my.bob.core.domain.board.entity.BoardComment;
import com.my.bob.core.domain.board.service.BoardMapperService;
import com.my.bob.core.domain.board.service.BoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

import static com.my.bob.core.util.DateConvertUtil.convertDateToString;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardConvertService implements BoardMapperService {

    private final BoardService boardService;

    private final ModelMapper modelMapper;


    public BoardDto convertBoardDto(long boardId) {
        Board board = boardService.getById(boardId);
        BoardDto dto = modelMapper.map(board, BoardDto.class);

        dto.setTitle(board.getBoardTitle());
        dto.setContent(board.getBoardContent());
        dto.setRegDate(convertDateToString(board.getRegDate(), "yyyy-MM-dd"));

        // later board 조회할 때 comments 함께 들고 오도록 변경할 것
        List<BoardComment> comments = board.getRootComments();
        dto.setCommentList(convertCommentList(comments));

        return dto;
    }

    public BoardTitleDto convertTitleDto(Board board) {
        BoardTitleDto dto = modelMapper.map(board, BoardTitleDto.class);

        if (board.isDelete()) {
            dto.setBoardTitle("삭제된 글입니다.");
            return dto;
        }

        String regDateStr = board.getRegDate().toLocalDate().toString();
        dto.setRegDate(regDateStr);

        return dto;
    }

    private BoardCommentDto convertCommentDto(BoardComment boardComment) {
        BoardCommentDto commentDto = modelMapper.map(boardComment, BoardCommentDto.class);
        commentDto.setContent(boardComment.getCommentContent());
        commentDto.setRegDate(convertDateToString(boardComment.getRegDate(), "yyyy-MM-dd"));

        List<BoardComment> childComments = boardComment.getChildComments();
        commentDto.setSubComments(convertCommentList(childComments));

        return commentDto;
    }

    private List<BoardCommentDto> convertCommentList(List<BoardComment> boardComments) {
        if (CollectionUtils.isEmpty(boardComments)) {
            return Collections.emptyList();
        }

        return boardComments
                .stream()
                .map(this::convertCommentDto)
                .toList();
    }

}
