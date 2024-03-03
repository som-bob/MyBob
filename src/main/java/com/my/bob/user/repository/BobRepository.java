package com.my.bob.user.repository;

import com.my.bob.user.entity.BobUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BobRepository extends JpaRepository<BobUser, Long> {
    
    Optional<BobUser> findByEmail(String email);
    boolean existsByEmail(String email);
}
