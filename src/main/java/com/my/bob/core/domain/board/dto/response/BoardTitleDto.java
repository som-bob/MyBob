package com.my.bob.core.domain.board.dto.response;

import lombok.Data;

@Data
public class BoardTitleDto {

    private long boardId;

    private String boardTitle;

    private String regId;

    private String regDate;
}
