package com.my.bob.v1.recipe.repository;

import com.my.bob.v1.recipe.entity.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IngredientRepository extends JpaRepository<Ingredient, Integer> {
}