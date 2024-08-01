package com.my.bob.board.dto;

import lombok.Builder;
import lombok.Data;

@Data
public class BoardSearchDto {

    private final int page;

    private final int size;

    private final String boardTitle;

    private final String boardContent;

    @Builder
    public BoardSearchDto(Integer page, Integer size, String boardTitle, String boardContent) {
        this.page = page == null? 0 : page;
        this.size = size == null? 0 : size;
        this.boardTitle = boardTitle;
        this.boardContent = boardContent;
    }
}
