package com.my.bob.stub;

import com.my.bob.core.external.s3.dto.response.FileSaveResponseDto;
import com.my.bob.core.external.s3.service.S3Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class S3StubService implements S3Service {
    @Override
    public FileSaveResponseDto uploadFile(MultipartFile file) {

        String stubFileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
        String stubUrl = "https://stub-s3.com/" + stubFileName;
        log.info("[STUB] 파일 업로드: {}, url: {}", file.getOriginalFilename(), stubUrl);

        return new FileSaveResponseDto(
                file.getOriginalFilename(),
                stubFileName,
                stubUrl,
                file.getContentType(),
                file.getSize()
        );
    }

    @Override
    public void deleteFile(String s3FileName) {
        log.info("[STUB] 파일 삭제: {}", s3FileName);
    }
}
