package com.my.bob.controller;

import com.my.bob.dto.CommonResponse;
import com.my.bob.dto.JoinUserDto;
import com.my.bob.dto.LoginDto;
import com.my.bob.dto.TokenDto;
import com.my.bob.exception.BadRequestException;
import com.my.bob.exception.DuplicateUserException;
import com.my.bob.exception.NonExistentUserException;
import com.my.bob.service.JoinService;
import com.my.bob.service.LoginService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class BobUserController {

    private final JoinService joinService;
    private final LoginService loginService;

    @PostMapping("/join")
    public CommonResponse joinMember(@Valid @RequestBody final JoinUserDto dto){
        CommonResponse commonResponse = new CommonResponse();

        try {
            joinService.joinMember(dto);
        } catch (DuplicateUserException e) {
            commonResponse.setError(HttpStatus.BAD_REQUEST, e.getMessage());
        }

        return commonResponse;
    }

    @PostMapping("/login")
    public CommonResponse login(@Valid @RequestBody final LoginDto dto) throws NonExistentUserException {

        TokenDto tokenDto = loginService.login(dto);
        return new CommonResponse(tokenDto);
    }

    @PostMapping("/reissue")
    public CommonResponse reissue(HttpServletRequest request){

        TokenDto tokenDto = null;
        try {
            tokenDto = loginService.reissue(request);
        } catch (BadRequestException e) {/* do nothing, handler global Handler */}
        return new CommonResponse(tokenDto);
    }

    // TODO
    @PostMapping("/logout")
    public CommonResponse logout() {

        return null;
    }
}
