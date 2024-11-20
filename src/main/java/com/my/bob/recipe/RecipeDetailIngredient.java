package com.my.bob.recipe;

import com.my.bob.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "bob_recipe_detail_ingredient")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RecipeDetailIngredient extends BaseTimeEntity {
    @Id
    @Column(name = "detail_ingredient_id", nullable = false)
    private Integer id;

    // TODO 연결하기
    @NotNull
    @Column(name = "recipe_detail_id", nullable = false)
    private Integer recipeDetailId;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ingredient_id", nullable = false)
    private Ingredient ingredient;

    @Size(max = 100)
    @Column(name = "amount", length = 100)
    private String amount;
}