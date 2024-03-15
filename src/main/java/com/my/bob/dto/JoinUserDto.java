package com.my.bob.dto;

import com.my.bob.constants.ErrorMessage;
import com.my.bob.constants.RegexType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class JoinUserDto {

    @Email
    @NotBlank(message = ErrorMessage.EMAIL_INVALID + ": ${email}")
    @Pattern(regexp = RegexType.EMAIL, message = ErrorMessage.EMAIL_INVALID + ": ${email}")
    private String email;

    @NotBlank(message = ErrorMessage.PASSWORD_INVALID)
    @Pattern(regexp = RegexType.PASSWORD, message = ErrorMessage.PASSWORD_INVALID)
    private String password;

    
}
