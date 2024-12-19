package com.my.bob.v1.refrigerator.service;

import com.my.bob.core.domain.member.entity.BobUser;
import com.my.bob.core.domain.member.service.BobUserService;
import com.my.bob.core.domain.refrigerator.dto.RefrigeratorCreateDto;
import com.my.bob.core.domain.refrigerator.dto.RefrigeratorDto;
import com.my.bob.core.domain.refrigerator.entity.Refrigerator;
import com.my.bob.core.domain.refrigerator.exception.RefrigeratorAlreadyExist;
import com.my.bob.core.domain.refrigerator.exception.RefrigeratorNotFoundException;
import com.my.bob.core.domain.refrigerator.repository.RefrigeratorRepository;
import com.my.bob.core.domain.refrigerator.service.RefrigeratorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RefrigeratorServiceImpl implements RefrigeratorService {

    private final BobUserService bobUserService;

    private final RefrigeratorRepository refrigeratorRepository;

    @Override
    @Transactional
    public RefrigeratorDto createRefrigerator(String email, RefrigeratorCreateDto dto) {
        BobUser bobUser = bobUserService.getByEmail(email);
        if(refrigeratorRepository.findOneByUser(bobUser).isPresent()) {
            throw new RefrigeratorAlreadyExist();
        }

        Refrigerator refrigerator = new Refrigerator(dto.getNickName(), bobUser);

        Refrigerator savedRefrigerator = refrigeratorRepository.save(refrigerator);
        return new RefrigeratorDto(savedRefrigerator);
    }

    @Override
    public RefrigeratorDto getRefrigerator(String email) {
        BobUser user = bobUserService.getByEmail(email);
        Optional<Refrigerator> optionalRefrigerator = refrigeratorRepository.findOneByUser(user);
        if(optionalRefrigerator.isEmpty()) {
            throw new RefrigeratorNotFoundException();
        }

        Refrigerator refrigerator = optionalRefrigerator.get();
        return new RefrigeratorDto(refrigerator);
    }
}
