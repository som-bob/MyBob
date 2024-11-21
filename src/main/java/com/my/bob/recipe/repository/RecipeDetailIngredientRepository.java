package com.my.bob.recipe.repository;

import com.my.bob.recipe.entity.RecipeDetailIngredient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecipeDetailIngredientRepository extends JpaRepository<RecipeDetailIngredient, Integer> {
}