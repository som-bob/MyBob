package com.my.bob.core.domain.refrigerator.service;

import com.my.bob.core.domain.member.entity.BobUser;
import com.my.bob.core.domain.recipe.entity.Ingredient;
import com.my.bob.core.domain.recipe.repository.IngredientRepository;
import com.my.bob.core.domain.refrigerator.dto.RefrigeratorAddIngredientDto;
import com.my.bob.core.domain.refrigerator.dto.RefrigeratorDto;
import com.my.bob.core.domain.refrigerator.entity.Refrigerator;
import com.my.bob.core.domain.refrigerator.entity.RefrigeratorIngredient;
import com.my.bob.core.domain.refrigerator.repository.RefrigeratorIngredientRepository;
import com.my.bob.core.domain.refrigerator.repository.RefrigeratorRepository;
import com.my.bob.v1.refrigerator.service.RefrigeratorIngredientServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.springframework.test.util.ReflectionTestUtils.setField;

class RefrigeratorIngredientServiceMockTest {

    @InjectMocks
    private RefrigeratorIngredientServiceImpl service;

    @Mock
    private RefrigeratorRepository refrigeratorRepository;

    @Mock
    private RefrigeratorIngredientRepository refrigeratorIngredientRepository;

    @Mock
    private IngredientRepository ingredientRepository;

    private BobUser mockUser;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
        mockUser = new BobUser("test_test@test.com", "<PASSWORD>", "Test User");
    }


    @Test
    @DisplayName("Mock 냉장고에 재료 추가 테스트")
    void addIngredient(){
        // given

        int refrigeratorId = 1;
        Refrigerator refrigerator = new Refrigerator("나의 냉장고", mockUser);
        setField(refrigerator, "id", refrigeratorId);

        int ingredientId = 100;
        Ingredient ingredient = new Ingredient("테스트 재료");
        setField(ingredient, "id", ingredientId);

        RefrigeratorAddIngredientDto dto = new RefrigeratorAddIngredientDto();
        dto.setIngredientId(ingredientId);
        dto.setAddedDate("2024-12-16");

        when(refrigeratorRepository.findById(refrigeratorId)).thenReturn(Optional.of(refrigerator));
        when(ingredientRepository.findById(ingredientId)).thenReturn(Optional.of(ingredient));
        when(refrigeratorIngredientRepository.existsByRefrigeratorAndIngredient(refrigerator, ingredient)).thenReturn(false);

        // when
        RefrigeratorDto result = service.addIngredient(refrigeratorId, dto);

        //then
        verify(refrigeratorIngredientRepository, times(1))
                .save(any(RefrigeratorIngredient.class));

        assertThat(result).isNotNull();
        assertThat(result.getNickName()).isEqualTo("나의 냉장고");
    }
}