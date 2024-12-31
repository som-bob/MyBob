package com.my.bob.v1.recipe.controller;

import com.my.bob.core.domain.base.dto.ResponseDto;
import com.my.bob.core.domain.recipe.dto.IngredientDto;
import com.my.bob.core.domain.recipe.service.IngredientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/ingredient")
@Tag(name = "Ingredient", description = "재료 관련 API") // Swagger 그룹 태그 설정
public class IngredientController {

    private final IngredientService ingredientService;

    @GetMapping
    @Operation(
            // API 간단한 설명
            summary = "모든 재료 조회",
            // API 상세 설명
            description = "DB에 저장된 모든 재료를 조회합니다. 결과는 이름 순으로 정렬됩니다.",
            // API가 속하는 그룹(Ingredient 관련)
            tags = {"Ingredient"}
    )
    // HTTP 상태 코드별 응답 상세
    // 각 상태 코드에 맞는 응답 본문(content)과 JSON 형식을 지정
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "성공적으로 재료 목록을 반환합니다.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ResponseDtoWithIngredientList.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "인증 실패 - 유효 하지 않은 JWT 토큰"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "서버 에러",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ResponseDto.class)
                    )
            )
    })
    public ResponseEntity<ResponseDto<List<IngredientDto>>> getAllIngredients() {
        List<IngredientDto> allIngredients = ingredientService.getAllIngredients();

        return ResponseEntity.ok(new ResponseDto<>(allIngredients));
    }


    @Schema(description = "재료 목록이 포함된 응답 DTO")
    private static class ResponseDtoWithIngredientList extends ResponseDto<List<IngredientDto>> {
    }
}
