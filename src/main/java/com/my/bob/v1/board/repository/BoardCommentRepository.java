package com.my.bob.v1.board.repository;

import com.my.bob.v1.board.entity.BoardComment;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoardCommentRepository extends JpaRepository<BoardComment, Long> {

    @Override
    @EntityGraph(attributePaths = {"board"})
    List<BoardComment> findAll();

}
