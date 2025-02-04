package com.my.bob.v1.recipe.service;

import com.my.bob.core.domain.file.entity.BobFile;
import com.my.bob.core.domain.recipe.dto.response.IngredientDto;
import com.my.bob.core.domain.recipe.entity.Ingredient;
import com.my.bob.core.domain.recipe.repository.IngredientRepository;
import com.my.bob.core.domain.recipe.service.IngredientService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class IngredientServiceImpl implements IngredientService {

    private final IngredientRepository ingredientRepository;

    @Override
    public List<IngredientDto> getAllIngredients() {
        List<Ingredient> ingredients = ingredientRepository.findAll();
        return ingredients.stream().map(ingredient -> {
                    String imageUrl = Optional.ofNullable(ingredient.getFile())
                            .map(BobFile::getFileUrl)
                            .orElse(null);

                    return IngredientDto.builder()
                            .id(ingredient.getId())
                            .ingredientName(ingredient.getIngredientName())
                            .ingredientDescription(ingredient.getIngredientDescription())
                            .imageUrl(imageUrl)
                            .build();
                })
                .sorted(Comparator.comparing(IngredientDto::getIngredientName))
                .toList();
    }
}
