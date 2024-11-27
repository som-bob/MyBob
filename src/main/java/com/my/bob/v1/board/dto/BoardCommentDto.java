package com.my.bob.v1.board.dto;

import lombok.Data;

import java.util.List;

@Data
public class BoardCommentDto {

    private long commentId;
    private String content;

    private boolean isDelete;

    private String regId;
    private String regDate;


    private List<BoardCommentDto> subComments;
}
