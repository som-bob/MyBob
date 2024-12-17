package com.my.bob.core.domain.refrigerator.service;

import com.my.bob.core.constants.ErrorMessage;
import com.my.bob.core.domain.refrigerator.entity.Refrigerator;
import com.my.bob.core.domain.refrigerator.repository.RefrigeratorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class RefrigeratorServiceHelper {

    private final RefrigeratorRepository refrigeratorRepository;
    public Refrigerator getRefrigerator(int refrigeratorId) {
        Optional<Refrigerator> optionalRefrigerator = refrigeratorRepository.findById(refrigeratorId);
        if (optionalRefrigerator.isEmpty()) {
            throw new IllegalArgumentException(ErrorMessage.NOT_EXISTENT_REFRIGERATOR);
        }

        return optionalRefrigerator.get();
    }
}
