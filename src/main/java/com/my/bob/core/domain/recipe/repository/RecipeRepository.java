package com.my.bob.core.domain.recipe.repository;

import com.my.bob.core.domain.recipe.entity.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecipeRepository extends JpaRepository<Recipe, Integer> {
}