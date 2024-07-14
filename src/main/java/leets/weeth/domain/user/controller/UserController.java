package leets.weeth.domain.user.controller;

import jakarta.validation.Valid;
import leets.weeth.domain.user.dto.UserDTO;
import leets.weeth.domain.user.service.UserService;
import leets.weeth.global.auth.annotation.CurrentUser;
import leets.weeth.global.common.error.exception.custom.BusinessLogicException;
import leets.weeth.global.common.response.CommonResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @PostMapping("/apply")
    public CommonResponse<String> apply(@RequestBody @Valid UserDTO.SignUp requestDto) {
        userService.signUp(requestDto);
        return CommonResponse.createSuccess();
    }

    @DeleteMapping("")
    public CommonResponse<String> delete(@CurrentUser Long userId) {
        userService.delete(userId);
        return CommonResponse.createSuccess();
    }

    @PostMapping("/apply/{cardinal}")
    public CommonResponse<String> applyOB(@CurrentUser Long userId, @PathVariable Integer cardinal) throws BusinessLogicException {
        userService.applyOB(userId, cardinal);
        return CommonResponse.createSuccess();
    }
}
