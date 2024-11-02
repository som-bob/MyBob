package com.my.bob.board.dto;

import lombok.Data;

@Data
public class BoardSearchDto {


    private String boardTitle;

    private String boardContent;

    private String regDateStart;    // yyyy-MM-dd
    private String regDateEnd;      // yyyy-MM-dd

}
