package com.my.bob.core.domain.recipe.entity;

import com.my.bob.core.domain.base.entity.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

@Getter
@Entity
@Table(name = "bob_recipe_ingredients", schema = "mybob")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RecipeIngredients extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "detail_ingredient_id", nullable = false)
    private Integer id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "recipe_id", nullable = false)
    private Recipe recipe;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ingredient_id", nullable = false)
    private Ingredient ingredient;

    @Column(name = "ingredient_detail_name")
    private String ingredientDetailName;

    @Size(max = 100)
    @Column(name = "amount", length = 100)
    private String amount;

    public RecipeIngredients(Recipe recipe, Ingredient ingredient,
                             String ingredientDetailName, String amount) {
        this.recipe = recipe;
        this.recipe.addIngredient(this);
        this.ingredient = ingredient;
        this.ingredientDetailName = StringUtils.isBlank(ingredientDetailName) ?
                ingredient.getIngredientName() : ingredientDetailName;
        this.amount = amount;
    }
}