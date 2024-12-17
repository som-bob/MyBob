package com.my.bob.core.domain.refrigerator.repository;

import com.my.bob.core.domain.recipe.entity.Ingredient;
import com.my.bob.core.domain.refrigerator.entity.Refrigerator;
import com.my.bob.core.domain.refrigerator.entity.RefrigeratorIngredient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefrigeratorIngredientRepository extends JpaRepository<RefrigeratorIngredient, Integer> {
    boolean existsByRefrigeratorAndIngredient(Refrigerator refrigerator, Ingredient ingredient);

    void deleteByRefrigerator(Refrigerator refrigerator);

}