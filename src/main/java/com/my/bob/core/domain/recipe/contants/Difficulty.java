package com.my.bob.core.domain.recipe.contants;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.my.bob.core.constants.interfaces.EnumPropertyType;

import java.util.Arrays;
import java.util.Optional;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum Difficulty implements EnumPropertyType {
    ANYONE("아무나"),
    BEGINNER("초급"),
    MIDRANGE("중급"),
    HIGH("고급"),
    GODHOOD("신의 경지"),

    ;

    private final String title;

    @Override
    public String getCode() {
        return name();
    }

    @Override
    public String getTitle() {
        return title;
    }

    Difficulty(String title) {
        this.title = title;
    }

    @JsonCreator
    public static Difficulty fromJson(@JsonProperty("code") String code) {
        try {
            return Difficulty.valueOf(code.toUpperCase());
        } catch (IllegalArgumentException | NullPointerException e) {
            return null;
        }
    }

    @JsonIgnore
    public static Optional<Difficulty> fromString(String code) {
        return Arrays.stream(Difficulty.values())
                .filter(difficulty -> difficulty.getCode().equalsIgnoreCase(code))
                .findFirst();
    }
}
