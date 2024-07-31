package leets.weeth.domain.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import leets.weeth.domain.user.entity.enums.Department;
import leets.weeth.domain.user.entity.enums.Position;
import leets.weeth.domain.user.entity.enums.Role;
import leets.weeth.domain.user.entity.enums.Status;

import java.time.LocalDateTime;
import java.util.List;

public class UserDTO {

    public record SignUp (
        @NotBlank        String name,
        @Email @NotBlank String email,
        @NotBlank        String password,
        @NotBlank        String studentId,
        @NotBlank        String tel,
        @NotNull         Position position,
        @NotNull         Department department,
        @NotNull         Integer cardinal
    ) {}

    public record Update (
            @NotBlank        String name,
            @Email @NotBlank String email,
            @NotBlank        String password,
            @NotBlank        String studentId,
            @NotBlank        String tel,
            @NotNull         Department department
    ) {}

    public record Response(
            Integer id,
            String name,
            String email,
            String studentId,
            String tel,
            String department,
            List<Integer> cardinals,
            Position position,
            Role role
    ) {}

    public record AdminResponse (
            Integer id,
            String name,
            String email,
            String studentId,
            String tel,
            String department,
            List<Integer> cardinals,
            Position position,
            Status status,
            Role role,
            Integer attendanceCount,
            Integer absenceCount,
            Integer attendanceRate,
            LocalDateTime createdAt,
            LocalDateTime modifiedAt
    ) {}
}
