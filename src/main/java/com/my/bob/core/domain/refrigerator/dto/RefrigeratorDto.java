package com.my.bob.core.domain.refrigerator.dto;

import com.my.bob.core.domain.refrigerator.entity.Refrigerator;
import lombok.Data;

import java.util.List;

@Data
public class RefrigeratorDto {

    private int refrigeratorId;

    private String nickName;

    private List<RefrigeratorIngredientDto> ingredients;

    public RefrigeratorDto(Refrigerator refrigerator) {
        this.refrigeratorId = refrigerator.getId();
        this.nickName = refrigerator.getNickname();
        this.ingredients = refrigerator.getBobRefrigeratorIngredients()
                .stream()
                .map(ingredient -> new RefrigeratorIngredientDto(ingredient.getIngredient(), ingredient.getDateAdded()))
                .toList();
    }
}