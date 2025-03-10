package com.my.bob.core.domain.member.service;

import com.my.bob.core.domain.member.dto.request.LoginDto;
import com.my.bob.core.domain.member.dto.response.TokenDto;
import com.my.bob.core.domain.member.exception.NonExistentUserException;
import com.my.bob.core.exception.BadRequestException;
import jakarta.servlet.http.HttpServletRequest;

public interface LoginService {

    TokenDto login(LoginDto dto) throws NonExistentUserException;

    TokenDto reissue(HttpServletRequest request) throws BadRequestException;
}
