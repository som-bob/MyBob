package com.my.bob.board.repository;

import com.my.bob.board.entity.Board;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import static com.my.bob.board.entity.QBoard.board;
import static com.my.bob.board.entity.QBoardComment.boardComment;

@Repository
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public void get(){
        // 삭제되지 않은 글 조회
        JPAQuery<Board> boards = jpaQueryFactory.select(board)
                .from(board.comments, boardComment)
                .where(board.isDelete.eq(false));
    }
}
