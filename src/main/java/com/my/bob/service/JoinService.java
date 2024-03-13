package com.my.bob.service;

import com.my.bob.dto.JoinUserDto;
import com.my.bob.entity.BobUser;
import com.my.bob.exception.DuplicateUserException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JoinService {

    private final BobUserService bobUserService;

    private final PasswordEncoder passwordEncoder;

    public void joinMember(JoinUserDto joinUserDto) throws DuplicateUserException {
        String email = joinUserDto.getEmail();
        boolean existByEmail = bobUserService.existByEmail(email);
        if(existByEmail) {
            throw new DuplicateUserException("이미 가입한 회원입니다.");
        }

        // 회원 가입
        String encodedPassword = passwordEncoder.encode(joinUserDto.getPassword());
        BobUser bobUser = BobUser.builder()
                .email(email)
                .password(encodedPassword)
                .build();
        bobUserService.save(bobUser);
    }

}
