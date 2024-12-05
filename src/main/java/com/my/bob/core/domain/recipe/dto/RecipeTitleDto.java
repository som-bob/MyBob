package com.my.bob.core.domain.recipe.dto;

import lombok.Data;

@Data
public class RecipeTitleDto {

    private Integer id;
    private String recipeName;
    private String difficulty;
    private Short cookingTime;
    private String imageUrl;
    private String servings;
    private String regId;
    private String regDate;
}