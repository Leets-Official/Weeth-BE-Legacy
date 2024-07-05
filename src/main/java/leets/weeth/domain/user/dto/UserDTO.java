package leets.weeth.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class UserDTO {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SignUp {
        private String username;
        private String password;
    }
}
