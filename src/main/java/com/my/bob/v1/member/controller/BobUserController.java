package com.my.bob.v1.member.controller;

import com.my.bob.core.constants.FailCode;
import com.my.bob.core.domain.base.dto.ResponseDto;
import com.my.bob.core.domain.member.dto.request.JoinUserDto;
import com.my.bob.core.domain.member.dto.request.LoginDto;
import com.my.bob.core.domain.member.dto.response.TokenDto;
import com.my.bob.core.domain.member.exception.DuplicateUserException;
import com.my.bob.core.domain.member.exception.NonExistentUserException;
import com.my.bob.core.domain.member.service.JoinService;
import com.my.bob.core.domain.member.service.LoginService;
import com.my.bob.core.exception.BadRequestException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/member")
public class BobUserController {

    private final JoinService joinService;
    private final LoginService loginService;

    @PostMapping("/join")
    public ResponseEntity<ResponseDto<Object>> joinMember(@Valid @RequestBody final JoinUserDto dto){

        try {
            joinService.joinMember(dto);
        } catch (DuplicateUserException e) {
            return ResponseEntity.badRequest().body(new ResponseDto<>(FailCode.V_00001, e.getMessage()));
        }

        return ResponseEntity.ok(new ResponseDto<>());
    }

    @PostMapping("/login")
    public ResponseEntity<ResponseDto<TokenDto>> login(@Valid @RequestBody final LoginDto dto)
            throws NonExistentUserException {

        TokenDto tokenDto = loginService.login(dto);
        return ResponseEntity.ok(new ResponseDto<>(tokenDto));
    }

    @PostMapping("/reissue")
    public ResponseEntity<ResponseDto<TokenDto>> reissue(HttpServletRequest request)
            throws BadRequestException {

        TokenDto tokenDto = loginService.reissue(request);
        return ResponseEntity.ok(new ResponseDto<>(tokenDto));
    }
}
