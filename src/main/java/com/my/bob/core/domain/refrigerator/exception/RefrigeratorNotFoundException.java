package com.my.bob.core.domain.refrigerator.exception;

import com.my.bob.core.constants.ErrorMessage;

public class RefrigeratorNotFoundException extends RefrigeratorException {

    public RefrigeratorNotFoundException() {
        super(ErrorMessage.NOT_EXISTENT_REFRIGERATOR);
    }
}
