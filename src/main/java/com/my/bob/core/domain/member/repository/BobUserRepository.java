package com.my.bob.core.domain.member.repository;

import com.my.bob.core.domain.member.entity.BobUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BobUserRepository extends JpaRepository<BobUser, Long> {
    
    Optional<BobUser> findOneByEmail(String email);
    boolean existsByEmail(String email);

}
