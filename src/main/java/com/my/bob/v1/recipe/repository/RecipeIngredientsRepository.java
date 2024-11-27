package com.my.bob.v1.recipe.repository;

import com.my.bob.v1.recipe.entity.RecipeIngredients;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecipeIngredientsRepository extends JpaRepository<RecipeIngredients, Integer> {
}