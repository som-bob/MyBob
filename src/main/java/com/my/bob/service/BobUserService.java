package com.my.bob.service;

import com.my.bob.entity.BobUser;
import com.my.bob.repository.BobRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BobUserService {

    private final BobRepository bobRepository;

    @Transactional(readOnly = true)
    public boolean existByEmail(String email) {
        return bobRepository.existsByEmail(email);
    }

    @Transactional
    public void save(BobUser bobUser) {
        bobRepository.save(bobUser);
    }
}
