package com.my.bob.board.dto;

import lombok.Data;

@Data
public class BoardTitleDto {

    private long boardId;

    private String boardTitle;

    private String regId;

    private String regDate;
}
