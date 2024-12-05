package com.my.bob.v1.refrigerator.repository;

import com.my.bob.core.domain.refrigerator.entity.RefrigeratorIngredient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefrigeratorIngredientRepository extends JpaRepository<RefrigeratorIngredient, Integer> {
}