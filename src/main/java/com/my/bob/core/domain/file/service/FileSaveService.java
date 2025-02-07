package com.my.bob.core.domain.file.service;

import com.my.bob.core.domain.file.entity.BobFile;
import org.springframework.web.multipart.MultipartFile;

public interface FileSaveService {

    BobFile uploadAndSaveFile(MultipartFile file);
}
