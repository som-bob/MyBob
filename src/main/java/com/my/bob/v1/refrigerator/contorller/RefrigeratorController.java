package com.my.bob.v1.refrigerator.contorller;

import com.my.bob.core.domain.base.dto.ResponseDto;
import com.my.bob.core.domain.refrigerator.dto.RefrigeratorCreateDto;
import com.my.bob.core.domain.refrigerator.dto.RefrigeratorDto;
import com.my.bob.core.domain.refrigerator.service.RefrigeratorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/refrigerator")
public class RefrigeratorController {

    private final RefrigeratorService refrigeratorService;

    @PostMapping
    public ResponseEntity<ResponseDto<RefrigeratorDto>> createRefrigerator(Principal principal,
                                                                           @RequestBody RefrigeratorCreateDto dto) {
        String email = principal.getName();
        RefrigeratorDto refrigeratorDto = refrigeratorService.createRefrigerator(email, dto);

        return ResponseEntity.ok(new ResponseDto<>(refrigeratorDto));
    }

}
