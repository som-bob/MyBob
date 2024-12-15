package com.my.bob.core.domain.refrigerator.service;

import com.my.bob.core.domain.refrigerator.dto.RefrigeratorCreateDto;
import com.my.bob.core.domain.refrigerator.dto.RefrigeratorDto;

public interface RefrigeratorService {

    RefrigeratorDto createRefrigerator(String email, RefrigeratorCreateDto dto);
}
