package com.my.bob.v1.member.service;

import com.my.bob.core.constants.ErrorMessage;
import com.my.bob.core.domain.member.entity.BobUser;
import com.my.bob.core.domain.member.repository.BobUserRepository;
import com.my.bob.core.domain.member.service.BobUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BobUserServiceImpl implements BobUserService {

    private final BobUserRepository bobUserRepository;

    public boolean existByEmail(String email) {
        return bobUserRepository.existsByEmail(email);
    }

    public BobUser getById(long userId) {
        return bobUserRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException(ErrorMessage.USER_CANNOT_BE_FOUND));
    }

    public BobUser getByEmail(String email) {
        return bobUserRepository.findOneByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(ErrorMessage.USER_CANNOT_BE_FOUND));
    }

    @Transactional
    public void save(BobUser bobUser) {
        bobUserRepository.save(bobUser);
    }

}
