package com.my.bob.v1.recipe.repository;

import com.my.bob.core.domain.recipe.contants.Difficulty;
import com.my.bob.core.domain.recipe.dto.RecipeListItemDto;
import com.my.bob.core.domain.recipe.dto.RecipeSearchDto;
import com.my.bob.core.domain.recipe.entity.Recipe;
import com.my.bob.core.domain.recipe.repository.RecipeQueryRepository;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;

import static com.my.bob.core.domain.recipe.entity.QIngredient.ingredient;
import static com.my.bob.core.domain.recipe.entity.QRecipe.recipe;
import static com.my.bob.core.domain.refrigerator.entity.QRecipeIngredients.recipeIngredients;

@Repository
@RequiredArgsConstructor
public class RecipeQueryRepositoryImpl implements RecipeQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;

    // TODO check 쿼리 실행 속도가 느리다면 recipe_id, ingredient_id, difficulty, recipe_name에 인덱스 추가
    @Override
    public Page<RecipeListItemDto> getByParam(Pageable pageable, RecipeSearchDto dto) {
        // 기본 쿼리 설정
        JPAQuery<Recipe> query = jpaQueryFactory
                .select(recipe)
                .from(recipe)
                .leftJoin(recipe.recipeIngredients, recipeIngredients).fetchJoin() // N + 1
                .leftJoin(recipeIngredients.ingredient, ingredient).fetchJoin()
                .where(
                        recipeNameContains(dto),
                        recipeDescriptionContains(dto),
                        ingredientIdsIn(dto),
                        difficultyEquals(dto)
                )
                .distinct();

        // 총 데이터 개수
        long total = Optional.ofNullable(jpaQueryFactory
                        .select(recipe.countDistinct())
                        .from(recipe)
                        .leftJoin(recipe.recipeIngredients, recipeIngredients)
                        .leftJoin(recipeIngredients.ingredient, ingredient)
                        .where(
                                recipeNameContains(dto),
                                recipeDescriptionContains(dto),
                                ingredientIdsIn(dto),
                                difficultyEquals(dto)
                        )
                        .fetchOne())
                .orElse(0L);

        // 페이지 처리
        List<Recipe> results = query
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return new PageImpl<>(results, pageable, total).map(RecipeListItemDto::new);
    }

    /* private conditions method */
    // 레시피 이름 조건
    private BooleanExpression recipeNameContains(RecipeSearchDto dto) {
        String recipeName = dto.getRecipeName();
        return StringUtils.hasText(recipeName) ?
                recipe.recipeName.containsIgnoreCase(recipeName) : null;
    }

    // 레시피 설명 조건
    private BooleanExpression recipeDescriptionContains(RecipeSearchDto dto) {
        String recipeDescription = dto.getRecipeDescription();
        return StringUtils.hasText(recipeDescription) ?
                recipe.recipeDescription.containsIgnoreCase(recipeDescription) : null;
    }

    // 재료 ID 조건
    private BooleanExpression ingredientIdsIn(RecipeSearchDto dto) {
        List<Integer> ingredientIds = dto.getIngredientIds();
        return ingredientIds != null && !ingredientIds.isEmpty()
                ? recipeIngredients.ingredient.id.in(ingredientIds)
                : null;
    }

    // 난이도 조건
    private BooleanExpression difficultyEquals(RecipeSearchDto dto) {
        Difficulty difficulty = dto.getDifficulty();
        return difficulty != null ? recipe.difficulty.eq(difficulty) : null;
    }
}
