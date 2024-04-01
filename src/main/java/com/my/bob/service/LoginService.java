package com.my.bob.service;

import com.my.bob.dto.LoginDto;
import com.my.bob.dto.TokenDto;
import com.my.bob.entity.BobUser;
import com.my.bob.exception.NonExistentUserException;
import com.my.bob.util.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LoginService {

    private final BobUserService bobUserService;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    public TokenDto login(LoginDto dto) throws NonExistentUserException {
        String email = dto.getEmail();
        if(! bobUserService.existByEmail(email)) {
            throw new NonExistentUserException("로그인 정보를 확인해주세요.");
        }

        BobUser user = bobUserService.getByEmail(email);
        if(! passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("비밀번호를 확인해주세요.");
        }

        // 마지막 로그인 일시 업데이트
        user.updateLastLoginDate();
        bobUserService.save(user);

        return jwtTokenProvider.generateTokenDto(email, user.getAuthority());
    }
}
