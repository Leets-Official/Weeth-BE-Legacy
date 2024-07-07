package leets.weeth.domain.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import leets.weeth.domain.user.entity.enums.Department;
import leets.weeth.domain.user.entity.enums.Position;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class UserDTO {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SignUp {
        @NotBlank        private String name;
        @Email @NotBlank private String email;
        @NotBlank        private String password;
        @NotBlank        private String studentId;
        @NotBlank        private String phoneNumber;
        @NotNull         private Position position;
        @NotNull         private Department department;
        @NotNull         private Integer cardinal;
    }
}
