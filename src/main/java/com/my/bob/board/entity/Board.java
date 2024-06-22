package com.my.bob.board.entity;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
@Table(name = "BOB_BOARD")
public class Board {

    @Id
    @Column(name = "BOARD_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long boardId;

    private String boardTitle;

    private String boardContent;


}
