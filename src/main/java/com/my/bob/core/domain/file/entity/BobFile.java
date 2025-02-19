package com.my.bob.core.domain.file.entity;

import com.my.bob.core.domain.base.entity.BaseRegEntity;
import com.my.bob.core.domain.file.constant.FileRoute;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
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

    @NotNull
    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted = Boolean.FALSE;

    @NotNull
    @Column(name = "route", nullable = false)
    @Enumerated(EnumType.STRING)
    private FileRoute route;

    public BobFile(String fileUrl,
                   String originalFileName, String fileName,
                   Long fileSize, String contentType,
                   FileRoute route) {
        this.fileUrl = fileUrl;
        this.originalFileName = originalFileName;
        this.fileName = fileName;
        this.fileSize = fileSize;
        this.contentType = contentType;
        this.route = route;
    }

    public void delete(){
        this.isDeleted = true;
    }
}
