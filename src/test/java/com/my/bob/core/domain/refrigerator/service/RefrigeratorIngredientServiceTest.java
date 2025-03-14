package com.my.bob.core.domain.refrigerator.service;

import com.my.bob.account.WithAccount;
import com.my.bob.core.constants.ErrorMessage;
import com.my.bob.core.domain.member.entity.BobUser;
import com.my.bob.core.domain.member.repository.BobUserRepository;
import com.my.bob.core.domain.recipe.entity.Ingredient;
import com.my.bob.core.domain.recipe.exception.IngredientNotFoundException;
import com.my.bob.core.domain.recipe.repository.IngredientRepository;
import com.my.bob.core.domain.refrigerator.dto.request.RefrigeratorAddIngredientDto;
import com.my.bob.core.domain.refrigerator.dto.response.RefrigeratorDto;
import com.my.bob.core.domain.refrigerator.dto.response.RefrigeratorInIngredientDto;
import com.my.bob.core.domain.refrigerator.entity.Refrigerator;
import com.my.bob.core.domain.refrigerator.entity.RefrigeratorIngredient;
import com.my.bob.core.domain.refrigerator.exception.RefrigeratorNotFoundException;
import com.my.bob.core.domain.refrigerator.repository.RefrigeratorIngredientRepository;
import com.my.bob.core.domain.refrigerator.repository.RefrigeratorRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.groups.Tuple.tuple;

@Transactional
@SpringBootTest
@ActiveProfiles("test")
@DisplayName("나의 냉장고 재료 테스트")
@WithAccount("test_test@test.com")   // 자동으로 해당 계정으로 들어가도록 세팅
class RefrigeratorIngredientServiceTest {

    @Autowired
    private RefrigeratorIngredientService service;

    @Autowired
    private BobUserRepository bobUserRepository;

    @Autowired
    private RefrigeratorRepository refrigeratorRepository;

    @Autowired
    private RefrigeratorIngredientRepository refrigeratorIngredientRepository;

    @Autowired
    private IngredientRepository ingredientRepository;

    private final String mockUserExistRefrigeratorEmail = "test_test@test.com";
    private final String mockUserNotExistRefrigeratorEmail = "test_fail@test.com";
    private Refrigerator refrigerator;
    private Ingredient ingredient1;
    private Ingredient ingredient2;

    @BeforeEach
    void setUp() {
        // 유저 데이터 저장
        bobUserRepository.save(new BobUser(mockUserNotExistRefrigeratorEmail, "<PASSWORD>", "냉장고 없는 유저"));
        BobUser mockUserExistRefrigerator =
                bobUserRepository.save(new BobUser(mockUserExistRefrigeratorEmail, "<PASSWORD>", "냉장고 있는 유저"));

        // 테스트 데이터 초기화
        ingredient1 = ingredientRepository.save(new Ingredient("테스트 저장 재료"));
        ingredient2 = ingredientRepository.save(new Ingredient("테스트 저장 재료 2"));

        refrigerator = refrigeratorRepository.save(new Refrigerator("나의 냉장고", mockUserExistRefrigerator));
    }

    @AfterEach
    void cleanUp() {
        refrigeratorIngredientRepository.deleteAllInBatch();
        refrigeratorRepository.deleteAllInBatch();
        ingredientRepository.deleteAllInBatch();
        bobUserRepository.deleteAllInBatch();
    }

    @Test
    @DisplayName("재료 추가 - 성공 케이스")
    void addIngredient_shouldAddNewIngredient() {
        // given
        RefrigeratorAddIngredientDto dto = new RefrigeratorAddIngredientDto();
        dto.setIngredientId(ingredient1.getId());
        dto.setAddedDate("2024-12-17");

        // when
        RefrigeratorDto result = service.addIngredient(refrigerator.getId(), dto);
        boolean existsBoolean =
                refrigeratorIngredientRepository.existsByRefrigeratorAndIngredient(refrigerator, ingredient1);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getNickName()).isEqualTo("나의 냉장고");
        assertThat(existsBoolean).isTrue();
    }

    @Test
    @DisplayName("재료 추가 - 재료 중복 추가 방지")
    void addIngredient_shouldNotAddDuplicateIngredient() {
        // given
        // 먼저 저장
        RefrigeratorIngredient refrigeratorIngredient = new RefrigeratorIngredient(refrigerator, ingredient1, LocalDate.now());
        refrigeratorIngredientRepository.save(refrigeratorIngredient);

        RefrigeratorAddIngredientDto dto = new RefrigeratorAddIngredientDto();
        dto.setIngredientId(ingredient1.getId());
        dto.setAddedDate("2024-12-17");

        // when
        RefrigeratorDto result = service.addIngredient(refrigerator.getId(), dto);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getIngredients()).hasSize(1); // 중복 저장해도 하나의 재료만 저장 되어 있다
    }

    @Test
    @DisplayName("재료 삭제 - 성공 후 재료 제거 확인")
    void deleteIngredient_shouldDeleteSuccessfully() {
        // given
        Integer refrigeratorId = refrigerator.getId();
        RefrigeratorIngredient refrigeratorIngredient = new RefrigeratorIngredient(refrigerator, ingredient1, LocalDate.now());
        refrigeratorIngredientRepository.save(refrigeratorIngredient);
        Integer refrigeratorIngredientId = refrigeratorIngredient.getId();

        // when
        service.deleteIngredient(refrigeratorId, refrigeratorIngredientId);

        // then
        assertThat(refrigeratorIngredientRepository.findById(refrigeratorIngredientId)).isEmpty();
    }


    @Test
    @DisplayName("재료 삭제 - 냉장고에 재료가 존재 하지 않는 경우 예외 발생")
    void deleteIngredient_shouldThrowException_WhenIngredientNotFound() {
        // given
        Integer refrigeratorId = refrigerator.getId();
        // 재료 저장
        RefrigeratorIngredient refrigeratorIngredient = new RefrigeratorIngredient(refrigerator, ingredient1, LocalDate.now());
        refrigeratorIngredientRepository.save(refrigeratorIngredient);
        Integer refrigeratorIngredientId = refrigeratorIngredient.getId();

        // 삭제 후에 또 삭제 시도 시 에러
        RefrigeratorDto result = service.deleteIngredient(refrigeratorId, refrigeratorIngredientId);

        // when & then
        assertThat(result).isNotNull();
        assertThatThrownBy(() -> service.deleteIngredient(refrigeratorId, refrigeratorIngredientId))
                .isInstanceOf(IngredientNotFoundException.class)
                .hasMessage(ErrorMessage.NOT_EXISTENT_INGREDIENT);
    }

    @Test
    @DisplayName("재료 모두 삭제 - 성공")
    void deleteAllIngredient_shouldDeleteAllIngredient() {
        // given
        Integer refrigeratorId = refrigerator.getId();
        refrigeratorIngredientRepository.save(new RefrigeratorIngredient(refrigerator, ingredient1, LocalDate.now()));
        refrigeratorIngredientRepository.save(new RefrigeratorIngredient(refrigerator, ingredient2, LocalDate.now()));

        // when
        RefrigeratorDto result = service.deleteAllIngredients(refrigeratorId);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getIngredients()).isEmpty();
    }

    @Test
    @DisplayName("로그인한 계정의 냉장고의 재료 리스트 조회 성공")
    void getAllIngredients_success_byLoginUser() {
        // given
        refrigeratorIngredientRepository.save(new RefrigeratorIngredient(refrigerator, ingredient1, LocalDate.now()));
        refrigeratorIngredientRepository.save(new RefrigeratorIngredient(refrigerator, ingredient2, LocalDate.now()));

        // when
        List<RefrigeratorInIngredientDto> result = service.getAllIngredients(mockUserExistRefrigeratorEmail);

        // then
        assertThat(result).isNotNull();
        assertThat(result).hasSize(2)
                .extracting("ingredientId", "ingredientName")
                .containsExactly(
                        tuple(ingredient1.getId(), ingredient1.getIngredientName()),
                        tuple(ingredient2.getId(), ingredient2.getIngredientName())
                );
    }

    @Test
    @DisplayName("로그인 한 계정의 냉장고 재료 리스트 조회 실패 - 해당 계정의 냉장고가 없음")
    void getAllIngredient_shouldThrow_whenRefrigeratorExist() {
        // when & then
        assertThatThrownBy(() -> service.getAllIngredients(mockUserNotExistRefrigeratorEmail))
                .isInstanceOf(RefrigeratorNotFoundException.class)
                .hasMessage(ErrorMessage.NOT_EXISTENT_REFRIGERATOR);
    }

}