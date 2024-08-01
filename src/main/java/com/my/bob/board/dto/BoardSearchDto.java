package com.my.bob.board.dto;

import lombok.Data;

@Data
public class BoardSearchDto {


    private String boardTitle;

    private String boardContent;

    private String regDateStart;
    private String regDateEnd;

}
