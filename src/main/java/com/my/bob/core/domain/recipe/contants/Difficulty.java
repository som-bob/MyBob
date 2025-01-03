package com.my.bob.core.domain.recipe.contants;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public enum Difficulty {
    BEGINNER("초급"),
    MIDRANGE("중급"),
    HIGH("고급"),
    GODHOOD("신의 경지"),
    ANYONE("아무나"),

    ;

    private String title;
}
