package com.my.bob.v1.refrigerator.service;

import com.my.bob.core.constants.ErrorMessage;
import com.my.bob.core.domain.recipe.entity.Ingredient;
import com.my.bob.core.domain.recipe.repository.IngredientRepository;
import com.my.bob.core.domain.refrigerator.dto.RefrigeratorAddIngredientDto;
import com.my.bob.core.domain.refrigerator.dto.RefrigeratorDto;
import com.my.bob.core.domain.refrigerator.entity.Refrigerator;
import com.my.bob.core.domain.refrigerator.entity.RefrigeratorIngredient;
import com.my.bob.core.domain.refrigerator.repository.RefrigeratorIngredientRepository;
import com.my.bob.core.domain.refrigerator.repository.RefrigeratorRepository;
import com.my.bob.core.domain.refrigerator.service.RefrigeratorIngredientService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RefrigeratorIngredientServiceImpl implements RefrigeratorIngredientService {

    private final RefrigeratorRepository refrigeratorRepository;
    private final IngredientRepository ingredientRepository;
    private final RefrigeratorIngredientRepository refrigeratorIngredientRepository;

    @Override
    public RefrigeratorDto addIngredient(int refrigeratorId, RefrigeratorAddIngredientDto dto) {
        Refrigerator refrigerator = getRefrigerator(refrigeratorId);
        Ingredient ingredient = getIngredient(dto.getIngredientId());

        // 이미 저장된 데이터가 있는지 검사
        if (refrigeratorIngredientRepository.existsByRefrigeratorAndIngredient(refrigerator, ingredient)) {
            return new RefrigeratorDto(refrigerator);
        }

        String addedDateStr = dto.getAddedDate();
        LocalDate addedDate = LocalDate.parse(addedDateStr);

        RefrigeratorIngredient refrigeratorIngredient = new RefrigeratorIngredient(refrigerator, ingredient, addedDate);
        refrigeratorIngredientRepository.save(refrigeratorIngredient);

        return new RefrigeratorDto(refrigerator);
    }

    @Override
    public RefrigeratorDto deleteIngredient(int refrigeratorId, int refrigeratorIngredientId) {
        Refrigerator refrigerator = getRefrigerator(refrigeratorId);

        RefrigeratorIngredient refrigeratorIngredient =
                refrigeratorIngredientRepository.findById(refrigeratorIngredientId)
                        .orElseThrow(() -> new IllegalArgumentException(ErrorMessage.NOT_EXISTENT_INGREDIENT));

        refrigerator.removeIngredient(refrigeratorIngredient);
        refrigeratorIngredientRepository.delete(refrigeratorIngredient);
        return new RefrigeratorDto(refrigerator);
    }

    @Override
    public RefrigeratorDto deleteAllIngredients(int refrigeratorId) {
        return null;
    }


    // private method
    private Refrigerator getRefrigerator(int refrigeratorId) {
        Optional<Refrigerator> optionalRefrigerator = refrigeratorRepository.findById(refrigeratorId);
        if (optionalRefrigerator.isEmpty()) {
            throw new IllegalArgumentException(ErrorMessage.NOT_EXISTENT_REFRIGERATOR);
        }

        return optionalRefrigerator.get();
    }

    private Ingredient getIngredient(int ingredientId) {
        Optional<Ingredient> optionalIngredient = ingredientRepository.findById(ingredientId);
        if (optionalIngredient.isEmpty()) {
            throw new IllegalArgumentException(ErrorMessage.NOT_EXISTENT_INGREDIENT);
        }

        return optionalIngredient.get();
    }
}
