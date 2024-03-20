package com.my.bob.service;

import com.my.bob.entity.BobUser;
import com.my.bob.repository.BobRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BobUserService {

    private final BobRepository bobRepository;

    public boolean existByEmail(String email) {
        return bobRepository.existsByEmail(email);
    }

    public BobUser getByEmail(String email) {
        return bobRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("해당 유저를 찾을 수 없습니다."));
    }

    @Transactional
    public void save(BobUser bobUser) {
        bobRepository.save(bobUser);
    }

    public Optional<BobUser> getByEmailAndPassword(String email, String password) {
        return bobRepository.findOneByEmailAndPassword(email, password);
    }
}
