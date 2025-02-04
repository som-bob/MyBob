package com.my.bob.core.domain.file.entity;

import com.my.bob.core.domain.base.entity.BaseRegEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "bob_file")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BobFile extends BaseRegEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "file_id", nullable = false)
    private long fileId;

    @Column(name = "file_url", nullable = false)
    private String fileUrl;

    @Column(name = "original_file_name")
    private String originalFileName;

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "file_size")
    private Long fileSize;

    @Column(name = "content_type")
    private String contentType;

    public BobFile(String fileUrl,
                   String originalFileName, String fileName,
                   Long fileSize, String contentType) {
        this.fileUrl = fileUrl;
        this.originalFileName = originalFileName;
        this.fileName = fileName;
        this.fileSize = fileSize;
        this.contentType = contentType;
    }
}
