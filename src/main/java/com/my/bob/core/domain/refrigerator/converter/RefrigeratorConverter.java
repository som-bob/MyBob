package com.my.bob.core.domain.refrigerator.converter;

import com.my.bob.core.domain.file.entity.BobFile;
import com.my.bob.core.domain.recipe.entity.Ingredient;
import com.my.bob.core.domain.refrigerator.dto.response.RefrigeratorDto;
import com.my.bob.core.domain.refrigerator.dto.response.RefrigeratorInIngredientDto;
import com.my.bob.core.domain.refrigerator.dto.response.RefrigeratorIngredientDto;
import com.my.bob.core.domain.refrigerator.entity.Refrigerator;

import java.util.Comparator;
import java.util.List;

public class RefrigeratorConverter {

    private RefrigeratorConverter() {
        throw new IllegalStateException("Utility class");
    }

    public static RefrigeratorDto convertDto(Refrigerator refrigerator) {
        List<RefrigeratorIngredientDto> refrigeratorIngredients = refrigerator.getBobRefrigeratorIngredients()
                .stream()
                .map(refrigeratorIngredient -> {
                    BobFile refrigeratorIngredientFile = refrigeratorIngredient.getIngredient().getFile();
                    return RefrigeratorIngredientDto.builder()
                            .ingredientId(refrigeratorIngredient.getId())
                            .ingredientName(refrigeratorIngredient.getIngredient().getIngredientName())
                            .ingredientUrl(refrigeratorIngredientFile == null ? null : refrigeratorIngredientFile.getFileUrl())
                            .addedDate(refrigeratorIngredient.getDateAdded().toString())
                            .build();
                })
                .sorted(Comparator.comparing(RefrigeratorIngredientDto::getIngredientName))
                .toList();

        return RefrigeratorDto.builder()
                .refrigeratorId(refrigerator.getId())
                .nickName(refrigerator.getNickname())
                .ingredients(refrigeratorIngredients)
                .build();
    }

    public static RefrigeratorInIngredientDto convertDto(Ingredient ingredient) {
        return RefrigeratorInIngredientDto.builder()
                .ingredientId(ingredient.getId())
                .ingredientName(ingredient.getIngredientName())
                .build();
    }
}
