package com.my.bob.v1.recipe.repository;

import com.my.bob.core.domain.recipe.contants.Difficulty;
import com.my.bob.core.domain.recipe.converter.RecipeConverter;
import com.my.bob.core.domain.recipe.dto.request.RecipeSearchDto;
import com.my.bob.core.domain.recipe.dto.response.RecipeListItemDto;
import com.my.bob.core.domain.recipe.entity.Recipe;
import com.my.bob.core.domain.recipe.repository.RecipeQueryRepository;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;

import static com.my.bob.core.domain.file.entity.QBobFile.bobFile;
import static com.my.bob.core.domain.recipe.entity.QIngredient.ingredient;
import static com.my.bob.core.domain.recipe.entity.QRecipe.recipe;
import static com.my.bob.core.domain.recipe.entity.QRecipeIngredients.recipeIngredients;

@Repository
@RequiredArgsConstructor
public class RecipeQueryRepositoryImpl implements RecipeQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;

    // TODO check 쿼리 실행 속도가 느리다면 recipe_id, ingredient_id, difficulty, recipe_name에 인덱스 추가
    @Override
    public Page<RecipeListItemDto> getByParam(Pageable pageable, RecipeSearchDto dto) {
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

        // 결과가 없을 경우 빈 페이지 반환
        if(total == 0){
            return Page.empty(pageable);
        }

        // 기본 Recipe Id 조회 쿼리
        List<Integer> recipeIds = jpaQueryFactory
                .select(recipe.id)
                .from(recipe)
                .leftJoin(recipe.recipeIngredients, recipeIngredients)
                .leftJoin(recipeIngredients.ingredient, ingredient)
                .where(
                        recipeNameContains(dto),
                        recipeDescriptionContains(dto),
                        ingredientIdsIn(dto),
                        difficultyEquals(dto)
                )
                .distinct()
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // Recipe 데이터를 ID 기반으로 조회 (페이징된 결과)
        List<Recipe> results = jpaQueryFactory
                .selectFrom(recipe)
                .leftJoin(recipe.recipeIngredients, recipeIngredients).fetchJoin()
                .leftJoin(recipeIngredients.ingredient, ingredient).fetchJoin()
                .leftJoin(recipe.file, bobFile).fetchJoin()
                .where(recipe.id.in(recipeIds))
                .fetch();

        return new PageImpl<>(results, pageable, total).map(RecipeConverter::convertDto);
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
        if (CollectionUtils.isEmpty(ingredientIds)) {
            return null;
        }

        // 조건 1: recipeIngredients.ingredient.id가 ingredientIds에 포함되어 있어야 함
        BooleanExpression includedInIngredientIds = recipeIngredients.ingredient.id.in(ingredientIds);

        // 조건 2: recipeIngredients.ingredient.id 중 ingredientIds에 없는 값이 있으면 안됨
        BooleanExpression noExtraIngredients =
                JPAExpressions
                        .selectOne()
                        .from(recipeIngredients)
                        .where(
                                // 현재 recipe와 매핑된 재료 (최상위 select 쿼리에서 조회된 recipe)
                                recipeIngredients.recipe.eq(recipe),
                                // ingredientIds에 없는 값이 있음
                                recipeIngredients.ingredient.id.notIn(ingredientIds)
                        )
                        .notExists(); // ingredientIds에 없는 값이 있는 레시피는 조회되지 않음

        // 조건 결합
        return includedInIngredientIds.and(noExtraIngredients);
    }

    // 난이도 조건
    private BooleanExpression difficultyEquals(RecipeSearchDto dto) {
        Difficulty difficulty = dto.getDifficulty();
        return difficulty != null ? recipe.difficulty.eq(difficulty) : null;
    }
}
