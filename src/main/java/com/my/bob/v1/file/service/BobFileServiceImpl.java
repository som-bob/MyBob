package com.my.bob.v1.file.service;

import com.my.bob.core.domain.file.constant.FileRoute;
import com.my.bob.core.domain.file.entity.BobFile;
import com.my.bob.core.domain.file.repsitory.BobFileRepository;
import com.my.bob.core.domain.file.service.BobFileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class BobFileServiceImpl implements BobFileService {

    private final BobFileRepository bobFileRepository;

    @Override
    public BobFile newFile(String fileUrl, String originalFilename, String fileName, long fileSize, String contentType,
                           FileRoute route) {
        BobFile bobFile = new BobFile(fileUrl, originalFilename, fileName, fileSize, contentType, route);

        return bobFileRepository.save(bobFile);
    }

    @Override
    public void deleteFile(long fileId) {
        BobFile bobFile = getBobFile(fileId);
        if(bobFile != null) {
            bobFile.delete();
        }
    }

    private BobFile getBobFile(long fileId) {
        Optional<BobFile> fileOptional = bobFileRepository.findById(fileId);
        return fileOptional.orElse(null);
    }
}
