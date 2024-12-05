package com.my.bob.v1.refrigerator.repository;

import com.my.bob.core.domain.refrigerator.entity.Refrigerator;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefrigeratorRepository extends JpaRepository<Refrigerator, Integer> {
}