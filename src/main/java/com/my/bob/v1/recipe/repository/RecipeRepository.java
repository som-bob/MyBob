package com.my.bob.v1.recipe.repository;

import com.my.bob.v1.recipe.entity.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecipeRepository extends JpaRepository<Recipe, Integer> {
}