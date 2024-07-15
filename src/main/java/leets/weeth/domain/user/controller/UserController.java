package leets.weeth.domain.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import leets.weeth.domain.user.dto.UserDTO;
import leets.weeth.domain.user.service.UserService;
import leets.weeth.global.auth.annotation.CurrentUser;
import leets.weeth.global.common.error.exception.custom.BusinessLogicException;
import leets.weeth.global.common.response.CommonResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @Operation(summary = "동아리 가입 신청")
    @PostMapping("/apply")
    public CommonResponse<String> apply(@RequestBody @Valid UserDTO.SignUp requestDto) {
        userService.signUp(requestDto);
        return CommonResponse.createSuccess();
    }

    @Operation(summary = "동아리 탈퇴")
    @DeleteMapping("")
    public CommonResponse<String> delete(@CurrentUser Long userId) {
        userService.delete(userId);
        return CommonResponse.createSuccess();
    }

    @Operation(summary = "동아리 OB 지원")
    @PostMapping("/apply/{cardinal}")
    public CommonResponse<String> applyOB(@CurrentUser Long userId, @PathVariable Integer cardinal) throws BusinessLogicException {
        userService.applyOB(userId, cardinal);
        return CommonResponse.createSuccess();
    }

    @Operation(summary = "동아리 회원 조회 (전체/기수별)")
    @GetMapping("/all")
    public CommonResponse<Map<Integer, List<UserDTO.Response>>> getUsers() {
        return CommonResponse.createSuccess(userService.findUsers());
    }
}
