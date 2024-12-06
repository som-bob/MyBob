package com.my.bob.core.domain.recipe.repository;

import com.my.bob.core.domain.recipe.entity.RecipeDetailIngredient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecipeDetailIngredientRepository extends JpaRepository<RecipeDetailIngredient, Integer> {
}