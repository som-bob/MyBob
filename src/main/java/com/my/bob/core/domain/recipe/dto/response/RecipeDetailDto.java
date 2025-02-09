package com.my.bob.core.domain.recipe.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RecipeDetailDto {

    private long recipeDetailId;
    private int recipeOrder;
    private String recipeDetailFileUrl;
    private String recipeDetailText;

    private String regDate;
    private String regId;
    private String modDate;
    private String modId;
}
