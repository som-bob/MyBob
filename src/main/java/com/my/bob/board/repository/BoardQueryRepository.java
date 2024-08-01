package com.my.bob.board.repository;

import com.my.bob.board.dto.BoardSearchDto;
import com.my.bob.board.entity.Board;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.my.bob.board.entity.QBoard.board;

@Repository
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;

    // 삭제된 글은 삭제된 글이라고 표시
    public Page<Board> getBoardList(BoardSearchDto dto, Pageable pageable) {
        List<Board> results = selectBoardBySearchDto(dto)
                .offset(pageable.getPageNumber())
                .limit(pageable.getPageSize())
                .orderBy(board.regDate.desc())
                .fetch();

        Long total = getBoardCountBySearchDto(dto);
        return PageableExecutionUtils.getPage(results, pageable, () -> total);
    }


    /* private method */
    private JPAQuery<Board> selectBoardBySearchDto(BoardSearchDto dto) {
        return jpaQueryFactory.select(board)
                .from(board)
                .where(titleExpression(dto))
                .where(contentExpression(dto));
    }

    private Long getBoardCountBySearchDto(BoardSearchDto dto) {
        Long total = jpaQueryFactory.select(board.count())
                .from(board)
                .where(titleExpression(dto))
                .where(contentExpression(dto))
                .fetchOne();
        return total == null ? 0 : total;
    }

    /* expression method */
    private BooleanExpression titleExpression(BoardSearchDto dto){
        String boardTitle = dto.getBoardTitle();
        if(StringUtils.isEmpty(boardTitle)) {
            return null;
        }

        return board.boardTitle.containsIgnoreCase(boardTitle);
    }

    private BooleanExpression contentExpression(BoardSearchDto dto){
        String boardContent = dto.getBoardContent();
        if(StringUtils.isEmpty(boardContent)) {
            return null;
        }

        return board.boardContent.containsIgnoreCase(boardContent);
    }

    private BooleanExpression betweenRegDate(BoardSearchDto dto){
        String regDateStart = dto.getRegDateStart();
        String regDateEnd = dto.getRegDateEnd();

        if(StringUtils.isEmpty(regDateStart) && StringUtils.isEmpty(regDateEnd)) {
            return null;
        } else if(StringUtils.isEmpty(regDateEnd)) {
            return null;

        } else if(StringUtils.isEmpty(regDateStart)) {
            return null;

        } else {
            return null;

        }
    }

}
