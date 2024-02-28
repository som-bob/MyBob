package com.my.bob.user.service;

import com.my.bob.user.entity.BobUser;
import com.my.bob.user.repository.BobRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BobUserService {

    private final BobRepository bobRepository;

    @Transactional
    public void save(BobUser bobUser) {
        bobRepository.save(bobUser);
    }
}
