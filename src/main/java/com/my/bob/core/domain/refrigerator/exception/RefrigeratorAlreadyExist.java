package com.my.bob.core.domain.refrigerator.exception;

import com.my.bob.core.constants.ErrorMessage;

public class RefrigeratorAlreadyExist extends RefrigeratorException {
    public RefrigeratorAlreadyExist() {
        super(ErrorMessage.ALREADY_CREATE_REFRIGERATOR);
    }
}
