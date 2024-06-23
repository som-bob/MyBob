package com.my.bob.board.dto;

import lombok.Data;

@Data
public class BoardCommentCreateDto {

    private Long parentCommentId;

    private String comment;
}
