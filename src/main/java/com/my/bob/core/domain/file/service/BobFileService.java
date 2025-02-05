package com.my.bob.core.domain.file.service;

import com.my.bob.core.domain.file.entity.BobFile;

public interface BobFileService {

    BobFile newFile(String fileUrl, String originalFilename, String fileName, long fileSize, String contentType);

    void deleteFile(long fileId);
}
