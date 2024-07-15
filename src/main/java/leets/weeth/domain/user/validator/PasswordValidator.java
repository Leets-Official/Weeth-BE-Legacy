package leets.weeth.domain.user.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import leets.weeth.domain.user.annotation.CheckPassword;
import leets.weeth.domain.user.dto.UserDTO;

public class PasswordValidator implements ConstraintValidator<CheckPassword, UserDTO.SignUp> {

    @Override
    public void initialize(CheckPassword constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(UserDTO.SignUp dto, ConstraintValidatorContext context) {
        return !dto.password().equals(dto.passwordConfirm());
    }
}
