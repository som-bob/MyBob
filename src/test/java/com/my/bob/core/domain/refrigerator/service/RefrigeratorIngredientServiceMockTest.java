package com.my.bob.core.domain.refrigerator.service;

import com.my.bob.core.constants.ErrorMessage;
import com.my.bob.core.domain.member.entity.BobUser;
import com.my.bob.core.domain.recipe.entity.Ingredient;
import com.my.bob.core.domain.recipe.exception.IngredientNotFoundException;
import com.my.bob.core.domain.recipe.repository.IngredientRepository;
import com.my.bob.core.domain.recipe.service.RecipeServiceHelper;
import com.my.bob.core.domain.refrigerator.dto.request.RefrigeratorAddIngredientDto;
import com.my.bob.core.domain.refrigerator.dto.response.RefrigeratorDto;
import com.my.bob.core.domain.refrigerator.entity.Refrigerator;
import com.my.bob.core.domain.refrigerator.entity.RefrigeratorIngredient;
import com.my.bob.core.domain.refrigerator.exception.RefrigeratorNotFoundException;
import com.my.bob.core.domain.refrigerator.repository.RefrigeratorIngredientRepository;
import com.my.bob.v1.refrigerator.service.RefrigeratorIngredientServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@DisplayName("Mock 나의 냉장고 재료 테스트")
class RefrigeratorIngredientServiceMockTest {

    @InjectMocks
    private RefrigeratorIngredientServiceImpl service;

    @Mock
    private RefrigeratorIngredientRepository refrigeratorIngredientRepository;

    @Mock
    private RefrigeratorServiceHelper refrigeratorServiceHelper;

    @Mock
    private RecipeServiceHelper recipeServiceHelper;

    @Mock
    private IngredientRepository ingredientRepository;

    private BobUser mockUser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockUser = new BobUser("test_test@test.com", "<PASSWORD>", "Test User");
    }


    @Test
    @DisplayName("Mock 재료 추가 - 성공 케이스")
    void addIngredient() {
        // given

        int refrigeratorId = 1;
        Refrigerator refrigerator = getMockRefrigerator(refrigeratorId);

        int ingredientId = 100;
        Ingredient ingredient = getMockIngredient(ingredientId);

        RefrigeratorAddIngredientDto dto = new RefrigeratorAddIngredientDto();
        dto.setIngredientId(ingredientId);
        dto.setAddedDate("2024-12-16");

        when(refrigeratorServiceHelper.getRefrigerator(refrigeratorId)).thenReturn(refrigerator);
        when(recipeServiceHelper.getIngredient(ingredientId)).thenReturn(ingredient);
        when(refrigeratorIngredientRepository.existsByRefrigeratorAndIngredient(refrigerator, ingredient)).thenReturn(false);

        // when
        RefrigeratorDto result = service.addIngredient(refrigeratorId, dto);

        //then
        verify(refrigeratorIngredientRepository, times(1))
                .save(any(RefrigeratorIngredient.class));

        assertThat(result).isNotNull();
        assertThat(result.getNickName()).isEqualTo("나의 냉장고");
    }

    @Test
    @DisplayName("Mock 재료 추가 - 냉장고가 존재 하지 않음(예외 발생)")
    void addIngredient_shouldThrowExceptionWhenRefrigeratorNotFound() {
        // given
        int refrigeratorId = 1;
        RefrigeratorAddIngredientDto dto = new RefrigeratorAddIngredientDto();
        dto.setIngredientId(100);
        dto.setAddedDate("2024-12-16");

        when(refrigeratorServiceHelper.getRefrigerator(refrigeratorId))
                .thenThrow(new RefrigeratorNotFoundException());

        // when & then
        assertThatThrownBy(() -> service.addIngredient(refrigeratorId, dto))
                .isInstanceOf(RefrigeratorNotFoundException.class).hasMessage(ErrorMessage.NOT_EXISTENT_REFRIGERATOR);
    }

    @Test
    @DisplayName("Mock 재료 추가 - 재료가 존재 하지 않음(예외 발생)")
    void addIngredient_shouldThrowExceptionWherIngredientNotFound() {
        // given
        int refrigeratorId = 1;
        int ingredientId = 100;

        Refrigerator refrigerator = getMockRefrigerator(refrigeratorId);

        RefrigeratorAddIngredientDto dto = new RefrigeratorAddIngredientDto();
        dto.setIngredientId(ingredientId);
        dto.setAddedDate("2024-12-16");

        // when
        when(refrigeratorServiceHelper.getRefrigerator(refrigeratorId)).thenReturn(refrigerator);
        when(recipeServiceHelper.getIngredient(ingredientId))
                .thenThrow(new IngredientNotFoundException());

        // then
        assertThatThrownBy(() -> service.addIngredient(refrigeratorId, dto))
                .isInstanceOf(IngredientNotFoundException.class).hasMessage(ErrorMessage.NOT_EXISTENT_INGREDIENT);
    }

    @Test
    @DisplayName("Mock 재료 추가 - 재료가 이미 존재(중복 저장 방지)")
    void addIngredient_shouldNotSaveWhenIngredientAlreadyExist() {
        // given
        int refrigeratorId = 1;
        int ingredientId = 100;

        Refrigerator refrigerator = getMockRefrigerator(refrigeratorId);
        Ingredient ingredient = getMockIngredient(ingredientId);

        RefrigeratorAddIngredientDto dto = new RefrigeratorAddIngredientDto();
        dto.setIngredientId(ingredientId);
        dto.setAddedDate("2024-12-16");

        when(refrigeratorServiceHelper.getRefrigerator(refrigeratorId)).thenReturn(refrigerator);
        when(recipeServiceHelper.getIngredient(ingredientId)).thenReturn(ingredient);
        when(refrigeratorIngredientRepository.existsByRefrigeratorAndIngredient(refrigerator, ingredient))
                .thenReturn(true);

        // when
        RefrigeratorDto result = service.addIngredient(refrigeratorId, dto);

        // then
        verify(refrigeratorIngredientRepository, never()).save(any(RefrigeratorIngredient.class));
        assertThat(result).isNotNull();
        assertThat(result.getNickName()).isEqualTo("나의 냉장고");
    }

    @Test
    @DisplayName("Mock 재료 삭제 - 냉장고가 존재 하지 않는 경우(예외 발생)")
    void deleteIngredient_shouldThrowException_WhenRefrigeratortNotFound() {
        // given
        int refrigeratorId = 1;
        int ingredientId = 100;

        when(refrigeratorServiceHelper.getRefrigerator(refrigeratorId))
                .thenThrow(new RefrigeratorNotFoundException());

        // when &  then
        assertThatThrownBy(() -> service.deleteIngredient(refrigeratorId, ingredientId))
                .isInstanceOf(RefrigeratorNotFoundException.class).hasMessage(ErrorMessage.NOT_EXISTENT_REFRIGERATOR);
    }

    @Test
    @DisplayName("Mock 재료 삭제 - 냉장고가 존재 하지 않음(예외 발생)")
    void deleteIngredient_shouldThrowException_WhenIngredientNotFound() {
        // given
        int refrigeratorId = 1;
        Refrigerator refrigerator = getMockRefrigerator(refrigeratorId);

        int ingredientId = 100;

        when(refrigeratorServiceHelper.getRefrigerator(refrigeratorId)).thenReturn(refrigerator);
        when(ingredientRepository.findById(ingredientId)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> service.deleteIngredient(refrigeratorId, ingredientId))
                .isInstanceOf(IngredientNotFoundException.class).hasMessage(ErrorMessage.NOT_EXISTENT_INGREDIENT);
    }

    @Test
    @DisplayName("Mock 재료 삭제 - 성공")
    void deleteIngredient_shouldDeleteIngredient() {
        // given
        int refrigeratorId = 1;
        int ingredientId = 100;
        int refrigeratorIngredientId = 1;

        Refrigerator refrigerator = getMockRefrigerator(refrigeratorId);
        Ingredient ingredient = getMockIngredient(ingredientId);
        RefrigeratorIngredient refrigeratorIngredient =
                getMockRefrigeratorIngredient(refrigeratorIngredientId, refrigerator, ingredient);

        when(refrigeratorServiceHelper.getRefrigerator(refrigeratorId)).thenReturn(refrigerator);
        when(recipeServiceHelper.getIngredient(ingredientId)).thenReturn(ingredient);
        when(refrigeratorIngredientRepository.findById(refrigeratorIngredientId))
                .thenReturn(Optional.of(refrigeratorIngredient));

        // when
        RefrigeratorDto result = service.deleteIngredient(refrigeratorId, refrigeratorIngredientId);

        // then
        verify(refrigeratorIngredientRepository, times(1)).delete(refrigeratorIngredient);
        assertThat(result).isNotNull();
        assertThat(result.getIngredients()).isEmpty();
    }

    @Test
    @DisplayName("Mock 재료 모두 삭제 - 냉장고가 존재 하지 않음(예외 발생)")
    void deleteAllIngredient_shouldThrowException_WhenRefrigeratorNotFound() {
        // given
        int refrigeratorId = 1;
        when(refrigeratorServiceHelper.getRefrigerator(refrigeratorId))
                .thenThrow(new RefrigeratorNotFoundException());

        // when & then
        assertThatThrownBy(() -> service.deleteAllIngredients(refrigeratorId))
                .isInstanceOf(RefrigeratorNotFoundException.class)
                .hasMessage(ErrorMessage.NOT_EXISTENT_REFRIGERATOR);
    }

    @Test
    @DisplayName("Mock 재료 모두 삭제 - 성공")
    void deleteAllIngredient_shouldDeleteAllIngredient() {
        // given
        int refrigeratorId = 1;
        Refrigerator refrigerator = getMockRefrigerator(refrigeratorId);
        RefrigeratorIngredient ri1 = getMockRefrigeratorIngredient(1, refrigerator, getMockIngredient(100));
        refrigerator.addIngredient(ri1);
        RefrigeratorIngredient ri2 = getMockRefrigeratorIngredient(2, refrigerator, getMockIngredient(101));
        refrigerator.addIngredient(ri2);

        when(refrigeratorServiceHelper.getRefrigerator(refrigeratorId)).thenReturn(refrigerator);

        // when
        RefrigeratorDto result = service.deleteAllIngredients(refrigeratorId);

        // then
        verify(refrigeratorIngredientRepository, times(1)).deleteByRefrigerator(refrigerator);
        assertThat(result).isNotNull();
        assertThat(result.getIngredients()).isEmpty();
    }

    private Refrigerator getMockRefrigerator(int refrigeratorId) {
        Refrigerator refrigerator = new Refrigerator("나의 냉장고", mockUser);
        ReflectionTestUtils.setField(refrigerator, "id", refrigeratorId);
        return refrigerator;
    }

    private static Ingredient getMockIngredient(int ingredientId) {
        Ingredient ingredient = new Ingredient("테스트 재료");
        ReflectionTestUtils.setField(ingredient, "id", ingredientId);
        return ingredient;
    }

    private RefrigeratorIngredient getMockRefrigeratorIngredient(int refrigeratorIngredientId,
                                                                 Refrigerator refrigerator,
                                                                 Ingredient ingredient) {
        RefrigeratorIngredient refrigeratorIngredient = new RefrigeratorIngredient(refrigerator, ingredient, LocalDate.now());
        ReflectionTestUtils.setField(refrigeratorIngredient, "id", refrigeratorIngredientId);
        return refrigeratorIngredient;
    }
}