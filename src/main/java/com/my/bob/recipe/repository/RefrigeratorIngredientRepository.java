package com.my.bob.recipe.repository;

import com.my.bob.recipe.entity.RefrigeratorIngredient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefrigeratorIngredientRepository extends JpaRepository<RefrigeratorIngredient, Integer> {
}