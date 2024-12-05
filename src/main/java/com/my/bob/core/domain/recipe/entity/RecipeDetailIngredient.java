package com.my.bob.core.domain.recipe.entity;

import com.my.bob.core.domain.base.entity.BaseTimeEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
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

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipe_detail_id", nullable = false)
    private RecipeDetail recipeDetail;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ingredient_id", nullable = false)
    private Ingredient ingredient;
}