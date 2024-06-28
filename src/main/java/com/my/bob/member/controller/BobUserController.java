package com.my.bob.member.controller;

import com.my.bob.common.dto.ResponseEntity;
import com.my.bob.exception.BadRequestException;
import com.my.bob.exception.DuplicateUserException;
import com.my.bob.exception.NonExistentUserException;
import com.my.bob.member.dto.JoinUserDto;
import com.my.bob.member.dto.LoginDto;
import com.my.bob.member.dto.TokenDto;
import com.my.bob.member.service.JoinService;
import com.my.bob.member.service.LoginService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
public class BobUserController {

    private final JoinService joinService;
    private final LoginService loginService;

    @PostMapping("/join")
    public ResponseEntity joinMember(@Valid @RequestBody final JoinUserDto dto){
        ResponseEntity responseEntity = new ResponseEntity();

        try {
            joinService.joinMember(dto);
        } catch (DuplicateUserException e) {
            responseEntity.setError(HttpStatus.BAD_REQUEST, e.getMessage());
        }

        return responseEntity;
    }

    @PostMapping("/login")
    public ResponseEntity login(@Valid @RequestBody final LoginDto dto) throws NonExistentUserException {

        TokenDto tokenDto = loginService.login(dto);
        return new ResponseEntity(tokenDto);
    }

    @PostMapping("/reissue")
    public ResponseEntity reissue(HttpServletRequest request){

        TokenDto tokenDto = null;
        try {
            tokenDto = loginService.reissue(request);
        } catch (BadRequestException e) {/* do nothing, handler global Handler */}
        return new ResponseEntity(tokenDto);
    }

    // TODO
    @PostMapping("/logout")
    public ResponseEntity logout() {

        return null;
    }
}
