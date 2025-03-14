package com.my.bob.core.external.s3.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FileSaveResponseDto {

    private String originalFilename;
    private String fileName;
    private String fileUrl;
    private String contentType;
    private long fileSize;

}
