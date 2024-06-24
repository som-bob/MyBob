package com.my.bob.board.repository;

import com.my.bob.board.entity.Board;
import com.my.bob.board.entity.BoardComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoardCommentRepository extends JpaRepository<BoardComment, Long> {

    List<BoardComment> findAllByBoardAndParentCommentIsNullOrderByRegDateAsc(Board board);

}
