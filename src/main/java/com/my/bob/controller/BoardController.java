package com.my.bob.controller;

import com.my.bob.dto.CommonResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@Slf4j
@RestController
@RequestMapping("/board")
@RequiredArgsConstructor
public class BoardController {

    @GetMapping("/list")
    public CommonResponse getBoardList(Principal principal){
        CommonResponse commonResponse = new CommonResponse();

        commonResponse.setData("ok");

        return commonResponse;
    }

}
