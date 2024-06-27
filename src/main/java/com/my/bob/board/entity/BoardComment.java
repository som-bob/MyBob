package com.my.bob.board.entity;

import com.my.bob.common.entity.BaseRegEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Table(name = "BOB_BOARD_COMMENT")
@NoArgsConstructor(access = AccessLevel.PROTECTED)  // protected 생성자 허용
public class BoardComment extends BaseRegEntity {

    @Id
    @Column(name = "COMMENT_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long commentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "BOARD_ID",  nullable = false)
    private Board board;

    // self reference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private BoardComment parentComment;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "parentComment")
    private List<BoardComment> childComments;

    @Column(name = "comment_content", nullable = false, length = 1000)
    private String commentContent;

    @Column(nullable = false, columnDefinition = "TINYINT", length = 1)
    private boolean isDelete;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime modDate;

    public BoardComment(Board board, BoardComment parentComment, String content) {
        this.board = board;
        if(parentComment != null) {
            this.parentComment = parentComment;
        }
        this.commentContent = content;
        this.isDelete = false;
    }

    public String getCommentContent() {
        if(isDelete) {
            return "삭제된 댓글입니다.";
        }
        return commentContent;
    }

    public void updateComment(String context) {
        this.commentContent = context;
    }

    public void deleteComment(){
        this.isDelete = true;
    }

    public boolean isRegistrant(String requestUser) {
        return this.getRegId().equals(requestUser);
    }

}
