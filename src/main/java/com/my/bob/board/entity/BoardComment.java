package com.my.bob.board.entity;

import com.my.bob.common.entity.BaseRegEntity;
import jakarta.persistence.*;
import lombok.Getter;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "BOB_BOARD_COMMENT")
public class BoardComment extends BaseRegEntity {

    @Id
    @Column(name = "COMMENT_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long commentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "BOARD_ID",  nullable = false)
    private Board board;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private BoardComment parentComment;

    @Column(name = "comment_content", nullable = false, length = 1000)
    private String commentContent;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime modDate;
}
