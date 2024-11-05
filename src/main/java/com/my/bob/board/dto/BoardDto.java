package com.my.bob.board.dto;

import lombok.Data;

import java.util.List;

@Data
public class BoardDto {

    private long boardId;

    private String title;
    private String content;

    private boolean isDelete;

    private String regId;
    private String regDate;

    private List<BoardCommentDto> commentList;
}
