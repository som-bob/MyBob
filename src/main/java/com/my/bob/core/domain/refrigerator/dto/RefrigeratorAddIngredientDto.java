package com.my.bob.core.domain.refrigerator.dto;

import com.my.bob.core.constants.ErrorMessage;
import com.my.bob.core.constants.RegexType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class RefrigeratorAddIngredientDto {

    @NotNull(message = ErrorMessage.THIS_IS_REQUIRED + "(ID)")
    private int ingredientId;

    @NotNull(message = ErrorMessage.THIS_IS_REQUIRED + "(addedDate)")
    @Pattern(regexp = RegexType.DATE, message = ErrorMessage.INVALID_DATE)
    private String addedDate;
}
