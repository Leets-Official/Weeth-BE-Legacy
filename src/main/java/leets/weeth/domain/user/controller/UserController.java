package leets.weeth.domain.user.controller;

import jakarta.validation.Valid;
import leets.weeth.domain.user.dto.UserDTO;
import leets.weeth.domain.user.service.UserService;
import leets.weeth.global.common.response.CommonResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @PostMapping("/sign-up")
    public CommonResponse<String> signUp(@RequestBody @Valid UserDTO.SignUp requestDto) {
        userService.signUp(requestDto);
        return CommonResponse.createSuccess();
    }

    @DeleteMapping("")
    public CommonResponse<String> delete(@AuthenticationPrincipal User user) {
        userService.delete(user.getUsername());
        return CommonResponse.createSuccess();
    }

    @PostMapping("/sign-up/{cardinal}")
    public CommonResponse<String> applyOB(@AuthenticationPrincipal User user, @PathVariable Integer cardinal) {
        userService.applyOB(user.getUsername(), cardinal);
        return CommonResponse.createSuccess();
    }
}
