package com.my.bob.member.repository;

import com.my.bob.member.entity.BobUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BobRepository extends JpaRepository<BobUser, Long> {
    
    Optional<BobUser> findOneByEmail(String email);
    boolean existsByEmail(String email);

}
