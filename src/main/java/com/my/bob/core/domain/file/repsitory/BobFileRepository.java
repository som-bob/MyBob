package com.my.bob.core.domain.file.repsitory;

import com.my.bob.core.domain.file.entity.BobFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BobFileRepository extends JpaRepository<BobFile, Long> {
}
