package com.my.bob.board.repository;

import com.my.bob.board.dto.BoardSearchDto;
import com.my.bob.board.entity.Board;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageImpl;
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
    public void getBoardList(BoardSearchDto dto) {
        List<Board> results = jpaQueryFactory.select(board)
                .from(board)
                .where(titleExpression(dto))
                .where(contentExpression(dto))
                .offset(dto.getPage())
                .limit(dto.getSize())
                .orderBy(board.regDate.desc())
                .fetch();


        Long total = jpaQueryFactory.select(board.count())
                .from(board)
                .where(titleExpression(dto))
                .where(contentExpression(dto))
                .fetchOne();
        total = total == null ? 0 : total;

//        return new PageImpl<>(results, pageable, total);
    }


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


}
