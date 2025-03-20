package com.my.bob.v1.redis;

import com.google.gson.Gson;
import com.my.bob.core.domain.recipe.dto.response.RecipeDto;
import com.my.bob.core.domain.recipe.entity.Recipe;
import com.my.bob.core.external.redis.service.RecentRecipeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class RecentRecipeServiceImpl implements RecentRecipeService {

    private final Gson gson;
    private final RedisTemplate<String, String> redisTemplate;
    private final ListOperations<String, String> listOperations;

    @Override
    public void saveRecentRecipe(String email, RecipeDto recipe) {
        String userKey = getUserKey(email);

        String recipeJson = gson.toJson(recipe);

        gson.fromJson(recipeJson, Recipe.class);
    }

    @Override
    public Optional<RecipeDto> getRecentRecipe(String email, String id) {
        String userKey = getUserKey(email);

        return Optional.empty();
    }

    @Override
    public List<RecipeDto> getByEmail(String email) {
        String userKey = getUserKey(email);
        List<String> recipeJsons = listOperations.range(userKey, 0, -1);// 모든 리스트 반환
        if(CollectionUtils.isEmpty(recipeJsons)) {
            return Collections.emptyList();
        }

        return recipeJsons.stream().map(recipeJson -> gson.fromJson(recipeJson, RecipeDto.class)).toList();
    }

    @Override
    public void clearRecipe(String email) {
        String userKey = getUserKey(email);
        redisTemplate.delete(userKey);  // 특정 유저의 모든 기록 삭제
    }

    private String getUserKey(String email) {
        return "recent_recipes:" + email;
    }
}
