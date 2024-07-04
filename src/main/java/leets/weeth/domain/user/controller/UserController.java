package leets.weeth.domain.user.controller;

import jakarta.validation.Valid;
import leets.weeth.domain.user.dto.UserDTO;
import leets.weeth.domain.user.service.UserService;
import leets.weeth.global.common.response.CommonResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @PostMapping("/sign-up")
    public CommonResponse<String> signUp(@RequestBody @Valid UserDTO.SignUp requestDto) throws Exception {
        userService.signUp(requestDto);
        return CommonResponse.createSuccess();
    }
}
