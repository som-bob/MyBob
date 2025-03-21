package com.my.bob.core.domain.board.entity;

import com.my.bob.core.domain.base.entity.BaseRegEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.BatchSize;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Entity
@Getter
@Table(name = "BOB_BOARD")
@NoArgsConstructor(access = AccessLevel.PROTECTED)  // protected 생성자 허용
public class Board extends BaseRegEntity {

    @Id
    @Column(name = "BOARD_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long boardId;

    private String boardTitle;

    private String boardContent;

    @Column(nullable = false, columnDefinition = "TINYINT", length = 1)
    private boolean isDelete;

    @BatchSize(size = 1000)
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "board")
    List<BoardComment> comments;

    @LastModifiedDate
    private LocalDateTime modDate;

    public Board(String title, String content) {
        this.boardTitle = title;
        this.boardContent = content;
        this.comments = new ArrayList<>();
        this.isDelete = false;
    }

    public String getBoardTitle() {
        if(isDelete) {
            return "삭제된 글입니다.";
        }
        return boardTitle;
    }

    public String getBoardContent() {
        if(isDelete) {
            return "삭제된 글입니다.";
        }
        return boardContent;
    }

    public void updateBoard(String title, String content) {
        this.boardTitle = title;
        this.boardContent = content;
    }

    public void deleteBoard(){
        this.isDelete = true;
    }

    public boolean isRegistrant(String requestUser) {
        return this.getRegId().equals(requestUser);
    }

    public List<BoardComment> getRootComments(){
        if(this.comments == null) {
            return Collections.emptyList();
        }

        return this.comments.stream().filter(BoardComment::isRootComment).toList();
    }
}
