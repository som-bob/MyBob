package com.my.bob.dto;

import com.my.bob.constants.ErrorMessage;
import com.my.bob.constants.RegexType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class JoinUserDto {

    @Email(message = ErrorMessage.INVALID_EMAIL)
    private String email;

    @NotBlank(message = ErrorMessage.INVALID_PASSWORD)
    @Pattern(regexp = RegexType.PASSWORD, message = ErrorMessage.INVALID_PASSWORD)
    private String password;

    private String nickName;
}
