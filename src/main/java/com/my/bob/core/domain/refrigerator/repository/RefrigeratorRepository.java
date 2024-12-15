package com.my.bob.core.domain.refrigerator.repository;

import com.my.bob.core.domain.member.entity.BobUser;
import com.my.bob.core.domain.refrigerator.entity.Refrigerator;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefrigeratorRepository extends JpaRepository<Refrigerator, Integer> {
    boolean existsByUser(BobUser user);


}