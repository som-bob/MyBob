package com.my.bob.v1.file.service;

import com.my.bob.core.domain.file.constant.FileRoute;
import com.my.bob.core.domain.file.entity.BobFile;
import com.my.bob.core.domain.file.service.BobFileService;
import com.my.bob.core.domain.file.service.FileSaveService;
import com.my.bob.core.external.s3.dto.response.FileSaveResponseDto;
import com.my.bob.core.external.s3.service.S3Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileSaveServiceImpl implements FileSaveService {

    private final S3Service s3Service;
    private final BobFileService bobFileService;

    @Override
    public BobFile uploadAndSaveFile(MultipartFile file, FileRoute route) {
        FileSaveResponseDto recipeFileSave = s3Service.uploadFile(file);
        return bobFileService.newFile(recipeFileSave.getFileUrl(),
                recipeFileSave.getOriginalFilename(),
                recipeFileSave.getFileName(),
                recipeFileSave.getFileSize(),
                recipeFileSave.getContentType(),
                route);
    }
}
