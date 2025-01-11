package com.my.bob.core.domain.recipe.repository;

import com.my.bob.core.domain.recipe.entity.RecipeIngredients;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecipeIngredientsRepository extends JpaRepository<RecipeIngredients, Integer> {
}