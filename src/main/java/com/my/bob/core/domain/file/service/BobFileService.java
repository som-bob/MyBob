package com.my.bob.core.domain.file.service;

public interface BobFileService {

    long newFile(String fileUrl, String originalFilename, String fileName, long fileSize, String contentType);

    void deleteFile(long fileId);
}
