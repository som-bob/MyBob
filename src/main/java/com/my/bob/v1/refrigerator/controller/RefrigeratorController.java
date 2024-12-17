package com.my.bob.v1.refrigerator.controller;

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

    /**
     * 제공된 데이터를 기반으로 지정된 사용자에 대한 새 냉장고를 만듭니다.
     * 이미 존재할 경우, 400 값을 응답 합니다.
     *
     * @param principal 사용자의 인증 세부 정보(예: 전자 메일)를 포함하는 보안 주체
     * @param dto 냉장고를 만드는 데 필요한 정보를 포함하는 데이터 전송 객체
     * @return 생성된 {@code RefrigeratorDto}와 함께 {@code ResponseDto}를 포함 하는 {@code ResponseEntity}
     */
    @PostMapping
    public ResponseEntity<ResponseDto<RefrigeratorDto>> createRefrigerator(Principal principal,
                                                                           @Valid @RequestBody RefrigeratorCreateDto dto) {
        String email = principal.getName();
        RefrigeratorDto refrigeratorDto = refrigeratorService.createRefrigerator(email, dto);

        return ResponseEntity.ok(new ResponseDto<>(refrigeratorDto));
    }

    /**
     * 인증된 사용자와 연결된 냉장고 데이터를 검색합니다.
     * 이미 존재 하지 않을 경우 400 값을 응답 합니다.
     * 
     * @param principal 인증된 사용자의 세부 정보를 포함하는 보안 주체(예: 이메일)
     * @return 생성된 {@code RefrigeratorDto}와 함께 {@code ResponseDto}를 포함 하는 {@code ResponseEntity}
     */
    @GetMapping
    public ResponseEntity<ResponseDto<RefrigeratorDto>> getRefrigerator(Principal principal) {
        String email = principal.getName();
        RefrigeratorDto refrigeratorDto = refrigeratorService.getRefrigerator(email);

        return ResponseEntity.ok(new ResponseDto<>(refrigeratorDto));
    }

    /**
     * 냉장고에 새 재료를 추가 합니다.
     *
     * @param refrigeratorId 채료를 추가할 냉장고의 ID 입니다.
     * @param dto 추가할 재료 정보 입니다.
     * @return 생성된 {@code RefrigeratorDto}와 함께 {@code ResponseDto}를 포함 하는 {@code ResponseEntity}
     */
    @PostMapping("/{refrigeratorId}/ingredient")
    public ResponseEntity<ResponseDto<RefrigeratorDto>> addIngredient(
            @PathVariable int refrigeratorId,
            @Valid @RequestBody RefrigeratorAddIngredientDto dto) {
        RefrigeratorDto refrigeratorDto = refrigeratorIngredientService.addIngredient(refrigeratorId, dto);

        return ResponseEntity.ok(new ResponseDto<>(refrigeratorDto));
    }

    /**
     * 해당 ID로 지정된 냉장고에서 해당 ID로 지정된 재료를 제거합니다.
     *
     * @param refrigeratorId 재료를 제거할 냉장고의 ID
     * @param refrigeratorIngredientId 제거할 냉장고 재료의 ID
     * @return 생성된 {@code RefrigeratorDto}와 함께 {@code ResponseDto}를 포함 하는 {@code ResponseEntity}
     */
    @DeleteMapping("/{refrigeratorId}/ingredient/{refrigeratorIngredientId}")
    public ResponseEntity<ResponseDto<RefrigeratorDto>> deleteIngredient(
            @PathVariable int refrigeratorId, @PathVariable int refrigeratorIngredientId) {
        RefrigeratorDto refrigeratorDto = refrigeratorIngredientService.deleteIngredient(refrigeratorId, refrigeratorIngredientId);
        return ResponseEntity.ok(new ResponseDto<>(refrigeratorDto));
    }

    /**
     * 지정된 냉장고 ID로 식별된 냉장고에서 모든 재료를 삭제합니다.
     *
     * @param refrigeratorId 모든 재료를 삭제할 냉장고의 ID
     * @return 모든 구성 요소 후에 업데이트된 {@code RefrigeratorDto}
     */
    @DeleteMapping("/{refrigeratorId}/ingredients")
    public ResponseEntity<ResponseDto<RefrigeratorDto>> deleteAllIngredients(
            @PathVariable int refrigeratorId) {
        RefrigeratorDto refrigeratorDto = refrigeratorIngredientService.deleteAllIngredients(refrigeratorId);
        return ResponseEntity.ok(new ResponseDto<>(refrigeratorDto));
    }

}
