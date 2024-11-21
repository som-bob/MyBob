package com.my.bob.recipe.repository;

import com.my.bob.recipe.entity.RecipeDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecipeDetailRepository extends JpaRepository<RecipeDetail, Integer> {
}