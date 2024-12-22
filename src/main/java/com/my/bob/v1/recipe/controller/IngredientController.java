package com.my.bob.v1.recipe.controller;

import com.my.bob.core.domain.base.dto.ResponseDto;
import com.my.bob.core.domain.recipe.dto.IngredientDto;
import com.my.bob.core.domain.recipe.service.IngredientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/ingredient")
public class IngredientController {

    private final IngredientService ingredientService;

    @GetMapping
    public ResponseEntity<ResponseDto<List<IngredientDto>>> getAllIngredients() {
        List<IngredientDto> allIngredients = ingredientService.getAllIngredients();

        return ResponseEntity.ok(new ResponseDto<>(allIngredients));
    }
}
