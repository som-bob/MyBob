package com.my.bob.core.domain.recipe.repository;

import com.my.bob.core.domain.refrigerator.entity.RecipeIngredients;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecipeIngredientsRepository extends JpaRepository<RecipeIngredients, Integer> {
}