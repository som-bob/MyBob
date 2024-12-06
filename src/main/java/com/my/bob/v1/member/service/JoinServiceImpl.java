package com.my.bob.v1.member.service;

import com.my.bob.core.domain.member.dto.JoinUserDto;
import com.my.bob.core.domain.member.entity.BobUser;
import com.my.bob.core.domain.member.exception.DuplicateUserException;
import com.my.bob.core.domain.member.service.JoinService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class JoinServiceImpl implements JoinService {

    private final BobUserServiceImpl bobUserService;
    private final PasswordEncoder passwordEncoder;

    public void joinMember(final JoinUserDto joinUserDto) throws DuplicateUserException {
        String email = joinUserDto.getEmail();
        if(bobUserService.existByEmail(email)) {
            throw new DuplicateUserException("사용 중인 이메일입니다.");
        }

        // 회원 가입
        String encodedPassword = passwordEncoder.encode(joinUserDto.getPassword());
        BobUser bobUser = BobUser.builder()
                .email(email)
                .password(encodedPassword)
                .nickName(joinUserDto.getNickName())
                .build();
        bobUserService.save(bobUser);
    }

}
