package com.my.bob.board.entity;

import com.my.bob.common.entity.BaseRegEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "BOB_BOARD")
public class Board extends BaseRegEntity {

    @Id
    @Column(name = "BOARD_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long boardId;

    private String boardTitle;

    private String boardContent;

    @Column(nullable = false, columnDefinition = "TINYINT", length = 1)
    private boolean isDelete;

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
}
