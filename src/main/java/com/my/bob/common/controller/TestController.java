package com.my.bob.common.controller;

import com.my.bob.common.dto.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {

    @GetMapping
    public ResponseEntity test(){
        return new ResponseEntity("test");
    }
}
