package com.my.bob.v1.recipe.repository;

import com.my.bob.v1.recipe.entity.Refrigerator;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefrigeratorRepository extends JpaRepository<Refrigerator, Integer> {
}