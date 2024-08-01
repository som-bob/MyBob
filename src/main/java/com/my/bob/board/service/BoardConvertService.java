package com.my.bob.board.service;

import com.my.bob.board.dto.BoardCommentDto;
import com.my.bob.board.dto.BoardDto;
import com.my.bob.board.entity.Board;
import com.my.bob.board.entity.BoardComment;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardConvertService {

    private final BoardService boardService;
    private final BoardCommentService boardCommentService;



    public BoardDto convertBoardDto(long boardId) {
        Board board = boardService.getById(boardId);

        BoardDto dto = new BoardDto();
        dto.setBoardId(boardId);
        dto.setTitle(board.getBoardTitle());
        dto.setContent(board.getBoardContent());
        dto.setDelete(board.isDelete());

        List<BoardComment> comments = boardCommentService.getRootCommentBy(board);
        if(CollectionUtils.isNotEmpty(comments)) {
            Set<Long> commentIdSet = new HashSet<>();
            List<BoardCommentDto> commentDtoList = comments
                    .stream()
                    .map(boardComment -> convertCommentDto(boardComment, commentIdSet))
                    .filter(Objects::nonNull)
                    .toList();
            dto.setCommentList(commentDtoList);
        }

        return dto;
    }

    /* private */
    private BoardCommentDto convertCommentDto(BoardComment boardComment, Set<Long> commentIdSet){
        long commentId = boardComment.getCommentId();
        if(commentIdSet.contains(commentId)) return null;

        BoardCommentDto commentDto = new BoardCommentDto();
        commentDto.setCommentId(commentId);
        commentDto.setContent(boardComment.getCommentContent());
        commentDto.setDelete(boardComment.isDelete());
        commentIdSet.add(commentId);

        List<BoardComment> childComment = boardComment.getChildComments();      // N + 1 문제?
        childComment.sort((o1, o2) -> (int) (o1.getCommentId() - o2.getCommentId()));
        if(CollectionUtils.isNotEmpty(childComment)) {
            List<BoardCommentDto> dtoList =
                    childComment
                            .stream()
                            .map(comment -> this.convertCommentDto(comment, commentIdSet))
                    .filter(Objects::nonNull).toList();
            commentDto.setSubComments(dtoList);
        }

        return commentDto;
    }
}
