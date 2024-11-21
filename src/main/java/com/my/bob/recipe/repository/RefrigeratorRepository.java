package com.my.bob.recipe.repository;

import com.my.bob.recipe.entity.Refrigerator;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefrigeratorRepository extends JpaRepository<Refrigerator, Integer> {
}