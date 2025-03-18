package com.my.bob.core.domain.recipe.repository;

import com.my.bob.core.domain.recipe.entity.RecentRecipe;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface RecentRecipeRepository extends CrudRepository<RecentRecipe, String> {

    List<RecentRecipe> findByEmail(String email);   // 특정 유저의 최근 본 레시피 조회
}
