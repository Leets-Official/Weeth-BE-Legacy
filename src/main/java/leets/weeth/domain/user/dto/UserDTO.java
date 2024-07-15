package leets.weeth.domain.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import leets.weeth.domain.user.annotation.CheckPassword;
import leets.weeth.domain.user.entity.enums.Department;
import leets.weeth.domain.user.entity.enums.Position;

public class UserDTO {

    public record SignUp (
        @NotBlank        String name,
        @Email @NotBlank String email,
        @NotBlank        String password,
        @NotBlank @CheckPassword String passwordConfirm,
        @NotBlank        String studentId,
        @NotBlank        String tel,
        @NotNull         Position position,
        @NotNull         Department department,
        @NotNull         Integer cardinal
    ) {}

    public record Response(
            Integer id,
            String name,
            String studentId,
            String tel,
            Department department,
            String email,
            Integer cardinal,
            Position position
    ) {}
}
