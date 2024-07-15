package leets.weeth.domain.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import leets.weeth.domain.user.entity.enums.Department;
import leets.weeth.domain.user.entity.enums.Position;

public class UserDTO {

    public record SignUp (
        @NotBlank        String name,
        @Email @NotBlank String email,
        @NotBlank        String password,
        @NotBlank        String studentId,
        @NotBlank        String phoneNumber,
        @NotNull         Position position,
        @NotNull         Department department,
        @NotNull         Integer cardinal
    ) {}

    public record Response(
            Integer id,
            String name,
            String studentId,
            Department department,
            String email,
            Integer cardinal,
            Position position
    ) {}
}
