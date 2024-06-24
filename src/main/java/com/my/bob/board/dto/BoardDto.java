package com.my.bob.board.dto;

import lombok.Data;

@Data
public class BoardDto {

    private String title;

    private String content;

    private boolean isDelete;
}
