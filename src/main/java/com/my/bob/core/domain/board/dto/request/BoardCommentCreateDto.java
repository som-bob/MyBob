package com.my.bob.core.domain.board.dto.request;

import lombok.Data;

@Data
public class BoardCommentCreateDto {

    private Long parentCommentId;

    private String comment;
}
