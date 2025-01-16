package com.my.bob.integration.refrigerator.controller;

import com.my.bob.account.WithAccount;
import com.my.bob.core.constants.ErrorMessage;
import com.my.bob.core.domain.base.dto.ResponseDto;
import com.my.bob.core.domain.member.repository.BobUserRepository;
import com.my.bob.core.domain.recipe.entity.Ingredient;
import com.my.bob.core.domain.recipe.repository.IngredientRepository;
import com.my.bob.core.domain.refrigerator.dto.request.RefrigeratorAddIngredientDto;
import com.my.bob.core.domain.refrigerator.dto.request.RefrigeratorCreateDto;
import com.my.bob.core.domain.refrigerator.dto.response.RefrigeratorDto;
import com.my.bob.core.domain.refrigerator.dto.response.RefrigeratorIngredientDto;
import com.my.bob.core.domain.refrigerator.repository.RefrigeratorIngredientRepository;
import com.my.bob.core.domain.refrigerator.repository.RefrigeratorRepository;
import com.my.bob.core.domain.refrigerator.service.RefrigeratorIngredientService;
import com.my.bob.core.domain.refrigerator.service.RefrigeratorService;
import com.my.bob.integration.common.IntegrationTestResponseValidator;
import com.my.bob.integration.util.IntegrationTestUtils;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.time.LocalDate;
import java.util.List;

import static com.my.bob.core.constants.FailCode.I_00002;
import static com.my.bob.core.constants.FailCode.R_00001;
import static com.my.bob.integration.common.IntegrationTestResponseValidator.assertFailResponse;
import static com.my.bob.integration.common.IntegrationTestResponseValidator.assertSuccessResponse;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)     // 테스트 클래스당 인스턴스 1개만 생성
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DisplayName("통합 테스트 - 냉장고 RefrigeratorController")
class RefrigeratorControllerIntegrationTest extends IntegrationTestUtils {
    // 냉장고 재료 삭제에 대한 테스트가 없습니다!

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private BobUserRepository bobUserRepository;

    @Autowired
    private RefrigeratorService refrigeratorService;

    @Autowired
    private RefrigeratorRepository refrigeratorRepository;

    @Autowired
    private RefrigeratorIngredientService refrigeratorIngredientService;

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
        token = getTokenFromTestUser();

        // 기본 재료 2개 이상 저장
        Ingredient save1 = ingredientRepository.save(new Ingredient("나_테스트 재료"));
        ingredientId1 = save1.getId();

        Ingredient save2 = ingredientRepository.save(new Ingredient("가_테스트 재료"));
        ingredientId2 = save2.getId();
    }

    @AfterEach
    void clearDatabase() {
        cleanUp(refrigeratorIngredientRepository, refrigeratorRepository, ingredientRepository, bobUserRepository);
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

    @Test
    @DisplayName("냉장고 재료 삭제 - 실패(존재 하지 않는 재료)")
    void deleteIngredient_fail_NotExistIngredient() {
        // given
        int refrigeratorId = createRefrigeratorAndGetId();
        int failToDeleteIngredientId = 999;

        // when & then
        webTestClient.delete()
                .uri(baseUrl + "/" + refrigeratorId + "/ingredient/" + failToDeleteIngredientId)
                .header("Authorization", "Bearer " + token)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(new ParameterizedTypeReference<ResponseDto<RefrigeratorDto>>() {
                })
                .value(responseDto ->
                        assertFailResponse(responseDto, I_00002, ErrorMessage.NOT_EXISTENT_INGREDIENT));
    }


    @Test
    @DisplayName("냉장고 재료 삭제 - 성공")
    void deleteIngredient_success() {
        // given
        int refrigeratorId = createRefrigeratorAndGetId();

        RefrigeratorAddIngredientDto addIngredientDto = new RefrigeratorAddIngredientDto();
        addIngredientDto.setIngredientId(ingredientId1);
        addIngredientDto.setAddedDate(LocalDate.now().toString());

        // when
        ResponseDto<RefrigeratorDto> responseBody = webTestClient.post()
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
                })
                .returnResult()
                .getResponseBody();

        // then 1 - 냉장고, 냉장고 재료 데이터가 있음
        assertThat(responseBody).isNotNull();
        assertThat(responseBody.getData()).isNotNull();
        RefrigeratorDto refrigeratorDto = responseBody.getData();
        List<RefrigeratorIngredientDto> ingredients = refrigeratorDto.getIngredients();
        assertThat(ingredients).hasSize(1);

        // then 2 - 재료 삭제
        RefrigeratorIngredientDto refrigeratorIngredientDto = ingredients.stream().findFirst().get();
        Integer ingredientId = refrigeratorIngredientDto.getIngredientId();
        webTestClient.delete()
                .uri(baseUrl + "/" + refrigeratorId + "/ingredient/" + ingredientId)
                .header("Authorization", "Bearer " + token)
                .exchange()
                .expectStatus().isOk()
                .expectBody(new ParameterizedTypeReference<ResponseDto<RefrigeratorDto>>() {
                })
                .value(responseDto -> {
                    assertSuccessResponse(responseDto);

                    RefrigeratorDto resultRefrigeratorDto = responseDto.getData();
                    assertThat(resultRefrigeratorDto).isNotNull();
                    assertThat(resultRefrigeratorDto.getIngredients()).isEmpty();
                });
    }

    @Test
    @DisplayName("냉장고 재료 모두 삭제 - 성공")
    void deleteAllIngredient_success() {
        // given - 재료 두개 추가한 냉장고
        RefrigeratorDto refrigeratorDto = createRefrigeratorAndAddIngredients();
        int refrigeratorId = refrigeratorDto.getRefrigeratorId();

        // when & then
        webTestClient.delete()
                .uri(baseUrl + "/" + refrigeratorId + "/ingredients")
                .header("Authorization", "Bearer " + token)
                .exchange()
                .expectStatus().isOk()
                .expectBody(new ParameterizedTypeReference<ResponseDto<RefrigeratorDto>>() {
                })
                .value(responseDto -> {
                    assertSuccessResponse(responseDto);

                    RefrigeratorDto resultRefrigeratorDto = responseDto.getData();
                    assertThat(resultRefrigeratorDto).isNotNull();
                    assertThat(resultRefrigeratorDto.getIngredients()).isEmpty();
                });
    }

    // TODO
    @Test
    @DisplayName("로그인한 계정의 냉장고 재료 리스트 조회 - 성공")
    void getAllIngredients_success() {
        // given

        // when

        // then
    }

    @Test
    @DisplayName("로그인 한 계정의 냉장고가 없을 때 재료 리스트 조회 - 실패")
    void getAllIngredient_fail_NotExistRefrigerator() {
        // given

        // when

        // then
    }

    /* 특정 단계에서 이미 생성된 데이터를 필요로 할경우 사용 */
    private int createRefrigeratorAndGetId() {
        RefrigeratorCreateDto refrigeratorCreateDto = new RefrigeratorCreateDto();
        refrigeratorCreateDto.setNickName("테스트 냉장고");
        assertTrue(bobUserRepository.findOneByEmail(getTestUserEmail()).isPresent());

        // 냉장고 생성
        return refrigeratorService.createRefrigerator(getTestUserEmail(), refrigeratorCreateDto).getRefrigeratorId();
    }

    private RefrigeratorDto createRefrigeratorAndAddIngredients() {
        // 냉장고 생성
        int refrigeratorId = createRefrigeratorAndGetId();

        // 재료 두 개 추가
        RefrigeratorAddIngredientDto addIngredientDto1 = new RefrigeratorAddIngredientDto();
        addIngredientDto1.setIngredientId(ingredientId1);
        addIngredientDto1.setAddedDate(LocalDate.now().toString());

        RefrigeratorAddIngredientDto addIngredientDto2 = new RefrigeratorAddIngredientDto();
        addIngredientDto2.setIngredientId(ingredientId2);
        addIngredientDto2.setAddedDate(LocalDate.now().toString());

        // when & then
        // 두 재료 추가 후 return
        refrigeratorIngredientService.addIngredient(refrigeratorId, addIngredientDto1);
        return refrigeratorIngredientService.addIngredient(refrigeratorId, addIngredientDto2);
    }

}