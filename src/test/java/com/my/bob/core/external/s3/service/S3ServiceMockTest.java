package com.my.bob.core.external.s3.service;

import com.my.bob.core.external.s3.dto.response.FileSaveResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mock.web.MockMultipartFile;
import software.amazon.awssdk.core.SdkResponse;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.http.SdkHttpResponse;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.DeleteObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@Slf4j
@DisplayName("S3 Mock 테스트")
@ExtendWith(MockitoExtension.class)
class S3ServiceMockTest {
    // s3 Mock 테스트

    @Mock
    private S3Client s3Client;

    @InjectMocks
    private S3ServiceImpl s3ServiceImpl;    // mock 테스트에서는 실제 구현 객체를 상속한다

    // 가상의 테스트 파일
    private final String testFileName = "mock_test.png";

    @Test
    @DisplayName("Mock S3 버킷 저장 테스트")
    void uploadFile_with_mock() throws IOException {
        // given
        // resource 파일 읽어 와서 적용
        ClassPathResource resource = new ClassPathResource("file/test.png");
        MockMultipartFile multipartFile = new MockMultipartFile(
                "file",
                "test.png",
                "image/png",
                resource.getInputStream());

        when(s3Client.putObject(any(PutObjectRequest.class), ArgumentMatchers.<RequestBody>any()))
                .thenReturn((PutObjectResponse) PutObjectResponse.builder()
                        .sdkHttpResponse(SdkHttpResponse.builder().statusCode(200).build())
                        .build());

        // when
        FileSaveResponseDto fileSaveResponseDto = s3ServiceImpl.uploadFile(multipartFile);

        // then
        assertThat(fileSaveResponseDto).isNotNull();
        assertThat(fileSaveResponseDto.getOriginalFilename()).isEqualTo("test.png");
        assertThat(fileSaveResponseDto.getFileName()).isNotBlank();
        assertThat(fileSaveResponseDto.getFileUrl()).isNotBlank();
        assertThat(fileSaveResponseDto.getFileSize()).isPositive();
        log.info("Mock file url : {}", fileSaveResponseDto.getFileUrl());

        verify(s3Client, times(1)).putObject(any(PutObjectRequest.class), ArgumentMatchers.<RequestBody>any());
    }
    
    @Test
    @DisplayName("Mock S3 버킷 삭제 테스트 - 성공")
    void deleteFile_success_with_mock() {
        // given
        SdkResponse sdkResponse = DeleteObjectResponse.builder()
                .sdkHttpResponse(SdkHttpResponse.builder().statusCode(204).build())
                .build();
        when(s3Client.deleteObject(any(DeleteObjectRequest.class))).thenReturn((DeleteObjectResponse) sdkResponse);

        // when
        s3ServiceImpl.deleteFile(testFileName);

        // then
        verify(s3Client, times(1)).deleteObject(any(DeleteObjectRequest.class));
    }

    @Test
    @DisplayName("Mock S3 버킷 삭제 테스트 - 실패")
    void deleteFile_fail_with_mock() {
        // given
        doThrow(new RuntimeException("S3 삭제 요청 실패")).when(s3Client).deleteObject(any(DeleteObjectRequest.class));

        // when & then
        try {
            s3ServiceImpl.deleteFile(testFileName);
        } catch (Exception e) {
            verify(s3Client, times(1)).deleteObject(any(DeleteObjectRequest.class));
        }
    }
}
