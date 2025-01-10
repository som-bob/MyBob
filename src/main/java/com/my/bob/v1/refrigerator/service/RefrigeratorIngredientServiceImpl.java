package com.my.bob.v1.refrigerator.service;

import com.my.bob.core.domain.recipe.entity.Ingredient;
import com.my.bob.core.domain.recipe.exception.IngredientNotFoundException;
import com.my.bob.core.domain.recipe.service.RecipeServiceHelper;
import com.my.bob.core.domain.refrigerator.dto.request.RefrigeratorAddIngredientDto;
import com.my.bob.core.domain.refrigerator.dto.response.RefrigeratorDto;
import com.my.bob.core.domain.refrigerator.entity.Refrigerator;
import com.my.bob.core.domain.refrigerator.entity.RefrigeratorIngredient;
import com.my.bob.core.domain.refrigerator.repository.RefrigeratorIngredientRepository;
import com.my.bob.core.domain.refrigerator.service.RefrigeratorIngredientService;
import com.my.bob.core.domain.refrigerator.service.RefrigeratorServiceHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RefrigeratorIngredientServiceImpl implements RefrigeratorIngredientService {

    private final RefrigeratorServiceHelper refrigeratorHelper;
    private final RecipeServiceHelper recipeServiceHelper;

    private final RefrigeratorIngredientRepository refrigeratorIngredientRepository;

    @Override
    @Transactional
    public RefrigeratorDto addIngredient(int refrigeratorId, RefrigeratorAddIngredientDto dto) {
        Refrigerator refrigerator = refrigeratorHelper.getRefrigerator(refrigeratorId);
        Ingredient ingredient = recipeServiceHelper.getIngredient(dto.getIngredientId());

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
    @Transactional
    public RefrigeratorDto deleteIngredient(int refrigeratorId, int refrigeratorIngredientId) {
        Refrigerator refrigerator = refrigeratorHelper.getRefrigerator(refrigeratorId);

        RefrigeratorIngredient refrigeratorIngredient =
                refrigeratorIngredientRepository
                        .findById(refrigeratorIngredientId).orElseThrow(IngredientNotFoundException::new);

        refrigerator.removeIngredient(refrigeratorIngredient);
        refrigeratorIngredientRepository.delete(refrigeratorIngredient);
        return new RefrigeratorDto(refrigerator);
    }

    @Override
    @Transactional
    public RefrigeratorDto deleteAllIngredients(int refrigeratorId) {
        Refrigerator refrigerator = refrigeratorHelper.getRefrigerator(refrigeratorId);

        refrigeratorIngredientRepository.deleteByRefrigerator(refrigerator);
        refrigerator.removeAllIngredients();

        return new RefrigeratorDto(refrigerator);
    }

}
