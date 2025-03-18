package com.my.bob.v1.recipe.service;

import com.my.bob.core.domain.recipe.repository.RecentRecipeRepository;
import com.my.bob.core.domain.recipe.service.RecentRecipeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class RecentRecipeServiceImpl implements RecentRecipeService {

    private final RecentRecipeRepository recentRecipeRepository;

    @Override
    public void saveRecentRecipe(int id, String email, String recipeName, String recipeJson) {

    }
}