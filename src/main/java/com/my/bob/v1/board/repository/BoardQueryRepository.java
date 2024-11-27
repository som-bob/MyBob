package com.my.bob.v1.board.repository;

import com.my.bob.v1.board.dto.BoardSearchDto;
import com.my.bob.v1.board.entity.Board;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.hibernate.type.descriptor.DateTimeUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static com.my.bob.v1.board.entity.QBoard.board;
import static com.my.bob.v1.util.DateConvertUtil.convertStringToDate;

@Repository
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;

    // 삭제된 글은 삭제된 글이라고 표시
    public Page<Board> getBoardList(BoardSearchDto dto, Pageable pageable) {
        List<Board> results = selectBoardBySearchDto(dto)
                .offset(pageable.getOffset())
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
                .where(
                        titleExpression(dto),
                        contentExpression(dto),
                        betweenRegDate(dto)
                );
    }

    private Long getBoardCountBySearchDto(BoardSearchDto dto) {
        Long total = jpaQueryFactory.select(board.count())
                .from(board)
                .where(
                        titleExpression(dto),
                        contentExpression(dto),
                        betweenRegDate(dto)
                )
                .fetchOne();
        return total == null ? 0 : total;
    }

    /* expression method */
    private BooleanExpression titleExpression(BoardSearchDto dto){
        String boardTitle = dto.getBoardTitle();
        if(StringUtils.isEmpty(boardTitle)) {
            return null;
        }

        return board.boardTitle.containsIgnoreCase(boardTitle).and(board.isDelete.eq(false));
    }

    private BooleanExpression contentExpression(BoardSearchDto dto){
        String boardContent = dto.getBoardContent();
        if(StringUtils.isEmpty(boardContent)) {
            return null;
        }

        return board.boardContent.containsIgnoreCase(boardContent).and(board.isDelete.eq(false));
    }

    private BooleanExpression betweenRegDate(BoardSearchDto dto){
        String regDateStartStr = dto.getRegDateStart();
        String regDateEndStr = dto.getRegDateEnd();

        // 날짜 정보 없음
        if(StringUtils.isEmpty(regDateStartStr) && StringUtils.isEmpty(regDateEndStr)) {
            return null;
        }

        // 날짜 존재. 비교
        LocalDateTime regDateStart = convertStringToDate(regDateStartStr, DateTimeUtils.FORMAT_STRING_DATE);
        LocalDateTime regDateEnd = convertStringToDate(regDateEndStr, DateTimeUtils.FORMAT_STRING_DATE);

        if(regDateEnd == null) {
            return board.regDate.goe(regDateStart).and(board.isDelete.eq(false));     // goe: regDate >= regDateStart
        }

        // regDateEnd 는 다음날 00:00:00로 세팅해서 비교
        regDateEnd = regDateEnd.plusDays(1);
        if(regDateStart == null) {
            return board.regDate.lt(regDateEnd).and(board.isDelete.eq(false));        // lt: regDate < regDateEnd (같거나로 하려면 loe)
        } else {
            return board.regDate.between(regDateStart, regDateEnd).and(board.isDelete.eq(false));
        }
    }

}
