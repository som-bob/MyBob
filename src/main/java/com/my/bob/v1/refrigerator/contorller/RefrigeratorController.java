package com.my.bob.v1.refrigerator.contorller;

import com.my.bob.core.domain.base.dto.ResponseDto;
import com.my.bob.core.domain.refrigerator.dto.RefrigeratorAddIngredientDto;
import com.my.bob.core.domain.refrigerator.dto.RefrigeratorCreateDto;
import com.my.bob.core.domain.refrigerator.dto.RefrigeratorDto;
import com.my.bob.core.domain.refrigerator.service.RefrigeratorIngredientService;
import com.my.bob.core.domain.refrigerator.service.RefrigeratorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/refrigerator")
public class RefrigeratorController {

    private final RefrigeratorService refrigeratorService;
    private final RefrigeratorIngredientService refrigeratorIngredientService;

    @PostMapping
    public ResponseEntity<ResponseDto<RefrigeratorDto>> createRefrigerator(Principal principal,
                                                                           @Valid @RequestBody RefrigeratorCreateDto dto) {
        String email = principal.getName();
        RefrigeratorDto refrigeratorDto = refrigeratorService.createRefrigerator(email, dto);

        return ResponseEntity.ok(new ResponseDto<>(refrigeratorDto));
    }

    @GetMapping
    public ResponseEntity<ResponseDto<RefrigeratorDto>> getRefrigerator(Principal principal) {
        String email = principal.getName();
        RefrigeratorDto refrigeratorDto = refrigeratorService.getRefrigerator(email);

        return ResponseEntity.ok(new ResponseDto<>(refrigeratorDto));
    }

    @PostMapping("/{ingredientId}/ingredient")
    public ResponseEntity<ResponseDto<RefrigeratorDto>> addIngredient(
            @PathVariable int ingredientId,
            @Valid @RequestBody RefrigeratorAddIngredientDto dto) {
        RefrigeratorDto refrigeratorDto = refrigeratorIngredientService.addIngredient(ingredientId, dto);

        return ResponseEntity.ok(new ResponseDto<>(refrigeratorDto));
    }

}
