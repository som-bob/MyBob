package com.my.bob.core.domain.refrigerator.repository;

import com.my.bob.core.domain.member.entity.BobUser;
import com.my.bob.core.domain.refrigerator.entity.Refrigerator;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefrigeratorRepository extends JpaRepository<Refrigerator, Integer> {
    Optional<Refrigerator> findOneByUser(BobUser user);

}