package com.my.bob.core.domain.member.dto;

import com.my.bob.core.constants.ErrorMessage;
import com.my.bob.core.constants.RegexType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class JoinUserDto {

    @NotBlank(message = ErrorMessage.EMPTY_EMAIL)
    @Email(message = ErrorMessage.INVALID_EMAIL)
    private String email;

    @NotBlank(message = ErrorMessage.EMPTY_PASSWORD)
    @Pattern(regexp = RegexType.PASSWORD, message = ErrorMessage.INVALID_PASSWORD)
    private String password;

    private String nickName;
}
