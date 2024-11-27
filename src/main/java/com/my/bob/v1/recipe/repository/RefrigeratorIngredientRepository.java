package com.my.bob.v1.recipe.repository;

import com.my.bob.v1.recipe.entity.RefrigeratorIngredient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefrigeratorIngredientRepository extends JpaRepository<RefrigeratorIngredient, Integer> {
}