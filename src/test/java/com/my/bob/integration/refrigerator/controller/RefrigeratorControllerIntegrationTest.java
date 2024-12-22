package com.my.bob.integration.refrigerator.controller;

import com.my.bob.account.WithAccount;
import com.my.bob.core.constants.ErrorMessage;
import com.my.bob.core.domain.base.dto.ResponseDto;
import com.my.bob.core.domain.member.repository.BobUserRepository;
import com.my.bob.core.domain.member.service.JoinService;
import com.my.bob.core.domain.member.service.LoginService;
import com.my.bob.core.domain.recipe.entity.Ingredient;
import com.my.bob.core.domain.recipe.repository.IngredientRepository;
import com.my.bob.core.domain.refrigerator.dto.RefrigeratorAddIngredientDto;
import com.my.bob.core.domain.refrigerator.dto.RefrigeratorCreateDto;
import com.my.bob.core.domain.refrigerator.dto.RefrigeratorDto;
import com.my.bob.core.domain.refrigerator.dto.RefrigeratorIngredientDto;
import com.my.bob.core.domain.refrigerator.repository.RefrigeratorIngredientRepository;
import com.my.bob.core.domain.refrigerator.repository.RefrigeratorRepository;
import com.my.bob.integration.common.IntegrationTestResponseValidator;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

import static com.my.bob.core.constants.FailCode.I_00002;
import static com.my.bob.core.constants.FailCode.R_00001;
import static com.my.bob.integration.common.IntegrationTestResponseValidator.assertFailResponse;
import static com.my.bob.integration.common.IntegrationTestResponseValidator.assertSuccessResponse;
import static com.my.bob.integration.util.IntegrationTestUtils.getTestUserEmail;
import static com.my.bob.integration.util.IntegrationTestUtils.getTokenFromTestUser;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)     // 테스트 클래스당 인스턴스 1개만 생성
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DisplayName("통합 테스트 - 냉장고 RefrigeratorController")
class RefrigeratorControllerIntegrationTest {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private JoinService joinService;

    @Autowired
    private LoginService loginService;

    @Autowired
    private BobUserRepository bobUserRepository;

    @Autowired
    private RefrigeratorRepository refrigeratorRepository;

    @Autowired
    private RefrigeratorIngredientRepository refrigeratorIngredientRepository;

    @Autowired
    private IngredientRepository ingredientRepository;

    private final String baseUrl = "/api/v1/refrigerator";
    private String token;
    private int ingredientId1;
    private int ingredientId2;

    @BeforeEach
    @WithAccount("system")
    void setUpDatabase() {
        token = getTokenFromTestUser(joinService, loginService);

        // 기본 재료 2개 이상 저장
        Ingredient ingredient1 = new Ingredient("나_테스트 재료");
        Ingredient save1 = ingredientRepository.save(ingredient1);
        ingredientId1 = save1.getId();

        Ingredient ingredient2 = new Ingredient("가_테스트 재료");
        Ingredient save2 = ingredientRepository.save(ingredient2);
        ingredientId2 = save2.getId();
    }

    @AfterEach
    void clearDatabase() {
        refrigeratorIngredientRepository.deleteAll();
        refrigeratorRepository.deleteAll();
        ingredientRepository.deleteAll();
        bobUserRepository.deleteAll();
    }

    @Test
    @DisplayName("냉장고 생성 - 성공")
    void createRefrigerator_success() {
        RefrigeratorCreateDto dto = new RefrigeratorCreateDto();
        dto.setNickName("테스트 냉장고");
        assertTrue(bobUserRepository.findOneByEmail(getTestUserEmail()).isPresent());

        webTestClient.post()
                .uri(baseUrl)
                .header("Authorization", "Bearer " + token)
                .bodyValue(dto)
                .exchange()
                .expectStatus().isOk()
                .expectBody(ResponseDto.class)
                .value(responseDto -> {
                    assertSuccessResponse(responseDto);
                    assertThat(responseDto.getData()).isNotNull();
                    assertThat(refrigeratorRepository.findAll()).isNotEmpty();
                })
        ;
    }

    @Test
    @DisplayName("냉장고 생성 - 실패(이미 존재 하는 냉장고 재요청)")
    void createRefrigerator_fail_ExistAlreadyRefrigerator() {
        RefrigeratorCreateDto dto = new RefrigeratorCreateDto();
        dto.setNickName("테스트 냉장고");
        assertTrue(bobUserRepository.findOneByEmail(getTestUserEmail()).isPresent());

        // 첫번째 시도 성공
        webTestClient.post()
                .uri(baseUrl)
                .header("Authorization", "Bearer " + token)
                .bodyValue(dto)
                .exchange()
                .expectStatus().isOk()
                .expectBody(ResponseDto.class)
                .value(responseDto -> {
                    assertSuccessResponse(responseDto);
                    assertThat(responseDto.getData()).isNotNull();
                    assertThat(refrigeratorRepository.findAll()).isNotEmpty();
                });

        // 두번째 시도 실패
        webTestClient.post()
                .uri(baseUrl)
                .header("Authorization", "Bearer " + token)
                .bodyValue(dto)
                .exchange()
                .expectStatus().is4xxClientError()
                .expectBody(ResponseDto.class)
                .value(responseDto ->
                        assertFailResponse(responseDto, R_00001, ErrorMessage.ALREADY_CREATE_REFRIGERATOR));
    }

    @Test
    @DisplayName("냉장고 조회 - 실패(존재 하지 않는 냉장고)")
    void getRefrigerator_fail_NotExistRefrigerator() {
        // 조회
        webTestClient.get()
                .uri(baseUrl)
                .header("Authorization", "Bearer " + token)
                .exchange()
                .expectStatus().is4xxClientError()
                .expectBody(ResponseDto.class)
                .value(responseDto ->
                        assertFailResponse(responseDto, R_00001, ErrorMessage.NOT_EXISTENT_REFRIGERATOR));
    }

    @Test
    @DisplayName("냉장고 조회 - 성공")
    void getRefrigerator_success() {
        RefrigeratorCreateDto dto = new RefrigeratorCreateDto();
        dto.setNickName("테스트 냉장고");
        assertTrue(bobUserRepository.findOneByEmail(getTestUserEmail()).isPresent());

        // 생성
        webTestClient.post()
                .uri(baseUrl)
                .header("Authorization", "Bearer " + token)
                .bodyValue(dto)
                .exchange()
                .expectStatus().isOk()
                .expectBody(new ParameterizedTypeReference<ResponseDto<RefrigeratorDto>>() {
                })
                .value(responseDto -> {
                    assertSuccessResponse(responseDto);

                    RefrigeratorDto data = responseDto.getData();
                    assertThat(data).isNotNull();
                    assertThat(data.getRefrigeratorId()).isPositive();

                    assertThat(refrigeratorRepository.findAll()).isNotEmpty();
                });

        // 조회
        webTestClient.get()
                .uri(baseUrl)
                .header("Authorization", "Bearer " + token)
                .exchange()
                .expectStatus().isOk()
                .expectBody(new ParameterizedTypeReference<ResponseDto<RefrigeratorDto>>() {
                })
                .value(responseDto -> {
                    assertSuccessResponse(responseDto);

                    RefrigeratorDto data = responseDto.getData();
                    assertThat(data).isNotNull();
                    assertThat(data.getRefrigeratorId()).isPositive();

                    assertThat(refrigeratorRepository.findAll()).isNotEmpty();
                });
    }

    @Test
    @DisplayName("냉장고 재료 추가 실패 - 냉장고가 존재 하지 않음")
    void addIngredient_fail_NotExistRefrigerator() {
        // given
        RefrigeratorAddIngredientDto dto = new RefrigeratorAddIngredientDto();
        dto.setIngredientId(ingredientId1);
        dto.setAddedDate(LocalDate.now().toString());

        int notExistRefrigeratorId = 999;

        webTestClient.post()
                .uri(baseUrl + "/" + notExistRefrigeratorId + "/ingredient")
                .bodyValue(dto)
                .header("Authorization", "Bearer " + token)
                .exchange()
                .expectStatus().is4xxClientError()
                .expectBody(ResponseDto.class)
                .value(responseDto -> {
                    assertFailResponse(responseDto, R_00001, ErrorMessage.NOT_EXISTENT_REFRIGERATOR);
                    assertThat(refrigeratorIngredientRepository.findAll()).isEmpty();
                });
    }

    @Test
    @DisplayName("냉장고 재고 추가 실패 - 존재 하지 않는 재료")
    void addIngredient_fail_NotExistIngredient() {
        // given
        int refrigeratorId = createRefrigeratorAndGetId();

        RefrigeratorAddIngredientDto addIngredientDto = new RefrigeratorAddIngredientDto();
        addIngredientDto.setIngredientId(999);
        addIngredientDto.setAddedDate(LocalDate.now().toString());

        webTestClient.post()
                .uri(baseUrl + "/" + refrigeratorId + "/ingredient")
                .bodyValue(addIngredientDto)
                .header("Authorization", "Bearer " + token)
                .exchange()
                .expectStatus().is4xxClientError()
                .expectBody(ResponseDto.class)
                .value(responseDto -> {
                    assertFailResponse(responseDto, I_00002, ErrorMessage.NOT_EXISTENT_INGREDIENT);

                    assertThat(refrigeratorIngredientRepository.findAll()).isEmpty();
                });
    }

    @Test
    @DisplayName("냉장고 재료 추가 - 성공")
    void addIngredient_success() {
        // given
        int refrigeratorId = createRefrigeratorAndGetId();

        RefrigeratorAddIngredientDto addIngredientDto = new RefrigeratorAddIngredientDto();
        addIngredientDto.setIngredientId(ingredientId1);
        addIngredientDto.setAddedDate(LocalDate.now().toString());

        // when & then
        webTestClient.post()
                .uri(baseUrl + "/" + refrigeratorId + "/ingredient")
                .bodyValue(addIngredientDto)
                .header("Authorization", "Bearer " + token)
                .exchange()
                .expectStatus().isOk()
                .expectBody(new ParameterizedTypeReference<ResponseDto<RefrigeratorDto>>() {
                })
                .value(responseDto -> {
                    assertSuccessResponse(responseDto);
                    assertThat(refrigeratorIngredientRepository.findAll()).isNotEmpty();
                });
    }

    @Test
    @DisplayName("냉장고 재료 추가 - 성공(2개 이상 재료 이름순 나열 확인)")
    void addIngredient_success_twoIngredient() {
        // given
        int refrigeratorId = createRefrigeratorAndGetId();

        RefrigeratorAddIngredientDto addIngredientDto1 = new RefrigeratorAddIngredientDto();
        addIngredientDto1.setIngredientId(ingredientId1);
        addIngredientDto1.setAddedDate(LocalDate.now().toString());

        RefrigeratorAddIngredientDto addIngredientDto2 = new RefrigeratorAddIngredientDto();
        addIngredientDto2.setIngredientId(ingredientId2);
        addIngredientDto2.setAddedDate(LocalDate.now().toString());

        // when & then
        // 첫번째 재료
        webTestClient.post()
                .uri(baseUrl + "/" + refrigeratorId + "/ingredient")
                .bodyValue(addIngredientDto1)
                .header("Authorization", "Bearer " + token)
                .exchange()
                .expectStatus().isOk()
                .expectBody(new ParameterizedTypeReference<ResponseDto<RefrigeratorDto>>() {
                })
                .value(IntegrationTestResponseValidator::assertSuccessResponse);

        // 두번째 재료
        webTestClient.post()
                .uri(baseUrl + "/" + refrigeratorId + "/ingredient")
                .bodyValue(addIngredientDto2)
                .header("Authorization", "Bearer " + token)
                .exchange()
                .expectStatus().isOk()
                .expectBody(new ParameterizedTypeReference<ResponseDto<RefrigeratorDto>>() {
                })
                .value(responseDto -> {
                    assertSuccessResponse(responseDto);

                    RefrigeratorDto refrigeratorDto = responseDto.getData();
                    assertThat(refrigeratorDto).isNotNull();

                    List<RefrigeratorIngredientDto> ingredients = refrigeratorDto.getIngredients();
                    assertThat(ingredients).hasSize(2);

                    assertThat(
                            ingredients.stream().map(RefrigeratorIngredientDto::getIngredientName).toList()
                    ).containsExactly("가_테스트 재료", "나_테스트 재료");
                });
    }


    private int createRefrigeratorAndGetId() {
        RefrigeratorCreateDto refrigeratorCreateDto = new RefrigeratorCreateDto();
        refrigeratorCreateDto.setNickName("테스트 냉장고");
        assertTrue(bobUserRepository.findOneByEmail(getTestUserEmail()).isPresent());

        // 냉장고 생성
        return Objects.requireNonNull(
                        webTestClient.post()
                                .uri(baseUrl)
                                .header("Authorization", "Bearer " + token)
                                .bodyValue(refrigeratorCreateDto)
                                .exchange()
                                .expectStatus().isOk()
                                .expectBody(new ParameterizedTypeReference<ResponseDto<RefrigeratorDto>>() {
                                })
                                .returnResult()
                                .getResponseBody()
                )
                .getData()
                .getRefrigeratorId();
    }
}