package com.my.bob.controller;

import com.my.bob.dto.CommonResponse;
import com.my.bob.dto.JoinUserDto;
import com.my.bob.exception.DuplicateUserException;
import com.my.bob.service.BobUserService;
import com.my.bob.service.JoinService;
import lombok.RequiredArgsConstructor;
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

    @PostMapping("/join")
    public CommonResponse joinMember(@RequestBody JoinUserDto dto){
        CommonResponse commonResponse = new CommonResponse();

        try {
            joinService.joinMember(dto);
        } catch (DuplicateUserException e) {
            commonResponse.setError(HttpStatus.BAD_REQUEST, e.getMessage());
        }

        return commonResponse;
    }
}
