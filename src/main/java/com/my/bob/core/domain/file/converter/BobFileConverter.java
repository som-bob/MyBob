package com.my.bob.core.domain.file.converter;

import com.my.bob.core.domain.file.entity.BobFile;

public class BobFileConverter {

    private BobFileConverter() {
        throw new IllegalStateException("Utility class");
    }

    public static String convertFileUrl(BobFile file) {
        return file != null ? file.getFileUrl() : null;
    }
}
