package com.my.bob.core.domain.member.dto.request;

import com.my.bob.core.constants.ErrorMessage;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginDto {

    @Email(message = ErrorMessage.INVALID_EMAIL)
    private String email;

    @NotBlank(message = ErrorMessage.EMPTY_PASSWORD)
    private String password;
}
