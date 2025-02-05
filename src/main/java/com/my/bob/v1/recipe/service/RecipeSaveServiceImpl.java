package com.my.bob.v1.recipe.service;

import com.my.bob.core.domain.file.entity.BobFile;
import com.my.bob.core.domain.file.repsitory.BobFileRepository;
import com.my.bob.core.domain.file.service.BobFileService;
import com.my.bob.core.domain.recipe.dto.request.RecipeCreateDto;
import com.my.bob.core.domain.recipe.entity.Recipe;
import com.my.bob.core.domain.recipe.repository.RecipeRepository;
import com.my.bob.core.domain.recipe.service.RecipeSaveService;
import com.my.bob.core.external.s3.dto.response.FileSaveResponseDto;
import com.my.bob.core.external.s3.service.S3Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@RequiredArgsConstructor
public class RecipeSaveServiceImpl implements RecipeSaveService {

    private final BobFileService bobFileService;
    private final S3Service s3Service;

    private final RecipeRepository recipeRepository;
    private final BobFileRepository bobFileRepository;

    @Override
    @Transactional
    public long newRecipe(RecipeCreateDto dto) {
        // 레시피 저장
        Recipe recipe = Recipe.builder()
                .recipeName(dto.getRecipeName())
                .recipeDescription(dto.getRecipeDescription())
                .cookingTime(dto.getCookingTime())
                .difficulty(dto.getDifficulty())
                .servings(dto.getServings())
                .build();

        Recipe savedRecipe = recipeRepository.save(recipe);

        // 레시피 파일 저장
        MultipartFile recipeFile = dto.getRecipeFile();
        if(recipeFile != null) {
            BobFile bobRecipeFile = getFileAfterSave(recipeFile);
            savedRecipe.setRecipeFile(bobRecipeFile);
        }

        // 재료 저장



        return savedRecipe.getId();
    }

    private BobFile getFileAfterSave(MultipartFile file) {
        FileSaveResponseDto recipeFileSave = s3Service.uploadFile(file);
        return bobFileService.newFile(recipeFileSave.getFileUrl(),
                recipeFileSave.getOriginalFilename(),
                recipeFileSave.getFileName(),
                recipeFileSave.getFileSize(),
                recipeFileSave.getContentType());
    }



}
