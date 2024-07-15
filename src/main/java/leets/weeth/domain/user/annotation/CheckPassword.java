package leets.weeth.domain.user.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import leets.weeth.domain.user.validator.PasswordValidator;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({FIELD})
@Retention(RUNTIME)
@Constraint(validatedBy = PasswordValidator.class)
public @interface CheckPassword {

    String message() default "비밀번호가 일치하지 않습니다.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}

