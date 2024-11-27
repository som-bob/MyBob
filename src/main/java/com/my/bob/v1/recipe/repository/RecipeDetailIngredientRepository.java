package com.my.bob.v1.recipe.repository;

import com.my.bob.v1.recipe.entity.RecipeDetailIngredient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecipeDetailIngredientRepository extends JpaRepository<RecipeDetailIngredient, Integer> {
}