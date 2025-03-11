package com.my.bob.v1.recipe.controller;

import com.my.bob.core.domain.base.dto.PageResponse;
import com.my.bob.core.domain.base.dto.ResponseDto;
import com.my.bob.core.domain.recipe.contants.Difficulty;
import com.my.bob.core.domain.recipe.dto.request.RecipeCreateDto;
import com.my.bob.core.domain.recipe.dto.request.RecipeSearchDto;
import com.my.bob.core.domain.recipe.dto.request.RecipeUpdateDto;
import com.my.bob.core.domain.recipe.dto.response.RecipeDto;
import com.my.bob.core.domain.recipe.dto.response.RecipeListItemDto;
import com.my.bob.core.domain.recipe.service.RecipeSaveService;
import com.my.bob.core.domain.recipe.service.RecipeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/recipe")
public class RecipeController {

    private final RecipeService recipeService;
    private final RecipeSaveService recipeSaveService;

    // 난이도 enum 조회
    @GetMapping("/difficulty")
    public ResponseEntity<ResponseDto<Difficulty[]>> getDifficulty() {
        return ResponseEntity.ok(new ResponseDto<>(Difficulty.values()));
    }

    // 레시피 리스트 조회
    @GetMapping
    public ResponseEntity<ResponseDto<PageResponse<RecipeListItemDto>>> getRecipe(Pageable pageable,
                                                                                  @ModelAttribute RecipeSearchDto dto) {
        Page<RecipeListItemDto> recipes = recipeService.getRecipes(pageable, dto);
        PageResponse<RecipeListItemDto> pageResponse = PageResponse.fromPage(recipes);

        return ResponseEntity.ok(new ResponseDto<>(pageResponse));
    }

    // 레시피 추가
    @PostMapping(value = "/new", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResponseDto<Integer>> createRecipe(
            @RequestPart("data") RecipeCreateDto dto,
            @RequestPart(value = "recipeFile", required = false) MultipartFile recipeFile,
            @RequestPart(value = "recipeDetailsFiles", required = false) MultipartFile[] recipeDetailsFiles) {

        int savedId = recipeSaveService.newRecipe(dto, recipeFile, recipeDetailsFiles);

        return ResponseEntity.ok(new ResponseDto<>(savedId));
    }

    // 레시피 조회
    @GetMapping("/{recipeId}")
    public ResponseEntity<ResponseDto<RecipeDto>> getRecipe(@PathVariable int recipeId) {
        return ResponseEntity.ok(new ResponseDto<>(recipeService.getRecipe(recipeId)));
    }

    // 레시피 조회
    @PutMapping(value = "/{recipeId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResponseDto<RecipeDto>> getRecipe(
            @PathVariable int recipeId,
            @RequestPart("data") RecipeUpdateDto dto,
            @RequestPart(value = "recipeFile", required = false) MultipartFile recipeFile,
            @RequestPart(value = "recipeDetailsFiles", required = false) MultipartFile[] recipeDetailsFiles) {
        recipeSaveService.updateRecipe(recipeId, dto, recipeFile, recipeDetailsFiles);

        return ResponseEntity.ok(new ResponseDto<>(recipeService.getRecipe(recipeId)));
    }

}
