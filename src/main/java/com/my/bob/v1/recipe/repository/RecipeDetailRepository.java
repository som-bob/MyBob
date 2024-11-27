package com.my.bob.v1.recipe.repository;

import com.my.bob.v1.recipe.entity.RecipeDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecipeDetailRepository extends JpaRepository<RecipeDetail, Integer> {
}