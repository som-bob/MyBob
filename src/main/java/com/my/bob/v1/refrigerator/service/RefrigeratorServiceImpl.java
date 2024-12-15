package com.my.bob.v1.refrigerator.service;

import com.my.bob.core.domain.member.entity.BobUser;
import com.my.bob.core.domain.member.service.BobUserService;
import com.my.bob.core.domain.refrigerator.dto.RefrigeratorCreateDto;
import com.my.bob.core.domain.refrigerator.dto.RefrigeratorDto;
import com.my.bob.core.domain.refrigerator.entity.Refrigerator;
import com.my.bob.core.domain.refrigerator.repository.RefrigeratorRepository;
import com.my.bob.core.domain.refrigerator.service.RefrigeratorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RefrigeratorServiceImpl implements RefrigeratorService {

    private final BobUserService bobUserService;

    private final RefrigeratorRepository refrigeratorRepository;

    @Override
    public RefrigeratorDto createRefrigerator(String email, RefrigeratorCreateDto dto) {
        BobUser bobUser = bobUserService.getByEmail(email);
        if(refrigeratorRepository.existsByUser(bobUser)) {
            throw new IllegalArgumentException("이미 냉장고를 생성한 유저입니다.");
        }

        Refrigerator refrigerator = new Refrigerator(dto.getNickName(), bobUser);

        Refrigerator savedRefrigerator = refrigeratorRepository.save(refrigerator);
        return new RefrigeratorDto(savedRefrigerator);
    }
}
