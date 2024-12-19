package com.my.bob.core.domain.refrigerator.dto;

import com.my.bob.core.domain.refrigerator.entity.Refrigerator;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class RefrigeratorDto {
    // 조회용

    private int refrigeratorId;

    private String nickName;

    private List<RefrigeratorIngredientDto> ingredients;

    public RefrigeratorDto(Refrigerator refrigerator) {
        this.refrigeratorId = refrigerator.getId();
        this.nickName = refrigerator.getNickname();
        this.ingredients = refrigerator.getBobRefrigeratorIngredients()
                .stream()
                .map(ingredient -> new RefrigeratorIngredientDto(ingredient.getIngredient(), ingredient.getDateAdded()))
                .sorted((o1, o2) -> o2.getIngredientName().compareTo(o1.getIngredientName()))
                .toList();
    }
}