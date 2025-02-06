package com.my.bob.core.domain.recipe.repository;

import com.my.bob.core.domain.recipe.entity.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface IngredientRepository extends JpaRepository<Ingredient, Integer> {
    List<Ingredient> findByIdIn(Collection<Integer> ids);

}