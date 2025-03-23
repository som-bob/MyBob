package com.my.bob.v1.redis;

import com.google.gson.Gson;
import com.my.bob.core.domain.recipe.dto.response.RecipeDto;
import com.my.bob.core.external.redis.service.RecentRecipeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class RecentRecipeServiceImpl implements RecentRecipeService {

    private final Gson gson;
    private final RedisTemplate<String, String> redisTemplate;
    private final ListOperations<String, String> listOperations;

    @Override
    public void saveRecentRecipe(String email, RecipeDto recipe) {
        String key = getUserRecentRecipesKey(email);
        String fullRecipeJson = getFullRecipe(recipe);

        // 유저별 리스트를 가지고 와, 기존에 본 레시피인지 검사 후 삭제
        List<String> recipes = listOperations.range(key, 0, -1);
        if(! CollectionUtils.isEmpty(recipes)) {
            for (String recipeJson : recipes) {
                // 지금 본 데이터가 기존에 redis 저장되어 있다면 삭제
                if(isSameRecipeId(recipeJson, recipe)) {
                    listOperations.remove(key, 0, recipeJson);
                    break;
                }
            }
        }

        // 최근 본 레시피 추가
        listOperations.leftPush(key, fullRecipeJson);

        // 10개 까지만 데이터 유지
        listOperations.trim(key, 0, (long) MAX_RECIPES - 1);
    }

    @Override
    public List<RecipeDto> getRecentRecipes(String email) {
        String userKey = getUserRecentRecipesKey(email);
        List<String> recipeJsons = listOperations.range(userKey, 0, -1);// 모든 리스트 반환
        if(CollectionUtils.isEmpty(recipeJsons)) {
            return Collections.emptyList();
        }

        return recipeJsons.stream().map(recipeJson -> {
            String onlyRecipeData = getOnlyRecipeData(recipeJson);  // 키와 json 분리
            if(! StringUtils.hasText(onlyRecipeData)) {
                return null;    // 빈 data 반환
            }

            return gson.fromJson(onlyRecipeData, RecipeDto.class);
        }).toList();
    }

    @Override
    public void clearRecipe(String email) {
        String userKey = getUserRecentRecipesKey(email);
        redisTemplate.delete(userKey);  // 특정 유저의 모든 기록 삭제
    }

    private String getUserRecentRecipesKey(String email) {
        return "recent_recipes:" + email;
    }

    private String getFullRecipe(RecipeDto recipe) {
        String recipeJson = gson.toJson(recipe);
        return String.format("%s:%s", recipe.getRecipeId(), recipeJson);
    }

    private boolean isSameRecipeId(String recipeJson, RecipeDto recipe) {
        return recipeJson.startsWith(recipe.getRecipeId() + ":");
    }

    private String getOnlyRecipeData(String recipeJson) {
        if(recipeJson.contains(":")) {
            String[] part = recipeJson.split(":", 2);
            return part[1];
        }

        return null;
    }
}
