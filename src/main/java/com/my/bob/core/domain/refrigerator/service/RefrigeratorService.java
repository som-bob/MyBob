package com.my.bob.core.domain.refrigerator.service;

import com.my.bob.core.domain.refrigerator.dto.request.RefrigeratorCreateDto;
import com.my.bob.core.domain.refrigerator.dto.response.RefrigeratorDto;

public interface RefrigeratorService {

    RefrigeratorDto createRefrigerator(String email, RefrigeratorCreateDto dto);
    RefrigeratorDto getRefrigerator(String email);
}
