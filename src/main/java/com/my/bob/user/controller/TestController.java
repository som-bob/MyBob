package com.my.bob.user.controller;

import com.my.bob.user.dto.CommonResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {

    @GetMapping
    public CommonResponse test(){
        return new CommonResponse("test");
    }
}
