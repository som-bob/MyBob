package com.my.bob.core.domain.board.dto;

import lombok.Data;

@Data
public class BoardCommentCreateDto {

    private Long parentCommentId;

    private String comment;
}
