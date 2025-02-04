package com.my.bob.core.domain.refrigerator.dto.response;

import lombok.Builder;
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

    @Builder
    public RefrigeratorDto(int refrigeratorId,
                           String nickName,
                           List<RefrigeratorIngredientDto> ingredients) {
        this.refrigeratorId = refrigeratorId;
        this.nickName = nickName;
        this.ingredients = ingredients;
    }
}