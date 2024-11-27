package com.my.bob.v1.board.dto;

import lombok.Data;

@Data
public class BoardCommentCreateDto {

    private Long parentCommentId;

    private String comment;
}
