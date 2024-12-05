package com.my.bob.v1.member.service;

import com.my.bob.core.config.JwtTokenProvider;
import com.my.bob.core.constants.AuthConstant;
import com.my.bob.core.constants.ErrorMessage;
import com.my.bob.core.domain.member.dto.LoginDto;
import com.my.bob.core.domain.member.dto.TokenDto;
import com.my.bob.core.domain.member.entity.BobUser;
import com.my.bob.core.domain.member.entity.BobUserRefreshToken;
import com.my.bob.core.exception.BadRequestException;
import com.my.bob.core.domain.member.exception.NonExistentUserException;
import com.my.bob.core.util.TokenUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class LoginService {

    private final BobUserService bobUserService;
    private final BobUserRefreshTokenService bobUserRefreshTokenService;

    private final PasswordEncoder passwordEncoder;

    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    public TokenDto login(LoginDto dto) throws NonExistentUserException {
        String email = dto.getEmail();
        if (!bobUserService.existByEmail(email)) {
            throw new NonExistentUserException("로그인 정보를 확인해주세요.");
        }

        BobUser user = bobUserService.getByEmail(email);
        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("비밀번호를 확인해주세요.");
        }

        // 마지막 로그인 일시 업데이트
        user.updateLastLoginDate();
        bobUserService.save(user);

        // refreshToken 추가
        TokenDto tokenDto = jwtTokenProvider.generateTokenDto(email, user.getAuthority());

        BobUserRefreshToken bobUserRefreshToken = new BobUserRefreshToken(user.getUserId(), tokenDto);
        bobUserRefreshTokenService.saveRefreshToken(bobUserRefreshToken);

        return tokenDto;
    }

    @Transactional
    public TokenDto reissue(HttpServletRequest request) throws BadRequestException {
        String requestHeader = request.getHeader(AuthConstant.AUTH_HEADER);
        if (StringUtils.isEmpty(requestHeader)) {
            throw new BadRequestException(ErrorMessage.INVALID_REQUEST);
        }

        // check refreshToken
        String token = TokenUtil.parsingToken(requestHeader);
        if (! bobUserRefreshTokenService.isExists(token)) {
            throw new BadRequestException(ErrorMessage.INVALID_REQUEST);
        }

        BobUserRefreshToken refreshToken = bobUserRefreshTokenService.getByToken(token);
        LocalDateTime expiryDate = refreshToken.getExpiryDate();
        if (expiryDate.isBefore(LocalDateTime.now())) {
            // 사용 기간이 지난 refreshToken
            bobUserRefreshTokenService.deleteByToken(token);
            throw new BadRequestException(ErrorMessage.INVALID_REQUEST);
        }

        // 정상 동작하는 refreshToken // 새로 발급 후 삭제
        long userId = refreshToken.getUserId();
        BobUser user = bobUserService.getById(userId);

        // 삭제
        bobUserRefreshTokenService.deleteByToken(token);

        // access Token 재발급
        TokenDto tokenDto = jwtTokenProvider.generateTokenDto(user.getEmail(), user.getAuthority());
        BobUserRefreshToken bobUserRefreshToken = new BobUserRefreshToken(user.getUserId(), tokenDto);
        bobUserRefreshTokenService.saveRefreshToken(bobUserRefreshToken);
        return tokenDto;
    }
}
