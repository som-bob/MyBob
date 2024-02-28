package com.my.bob.user.repository;

import com.my.bob.user.entity.BobUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BobRepository extends JpaRepository<BobUser, Long> {
}
