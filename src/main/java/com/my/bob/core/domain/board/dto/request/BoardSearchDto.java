package com.my.bob.core.domain.board.dto.request;

import lombok.Data;

@Data
public class BoardSearchDto {


    private String boardTitle;

    private String boardContent;

    private String regDateStart;    // yyyy-MM-dd
    private String regDateEnd;      // yyyy-MM-dd

}
