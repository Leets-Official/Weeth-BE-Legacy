package leets.weeth.domain.account.controller;

import leets.weeth.domain.account.dto.AccountDTO;
import leets.weeth.domain.account.service.AccountService;
import leets.weeth.global.common.response.CommonResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/account")
public class AccountController {

    private final AccountService accountService;

    @GetMapping("/{cardinal}")
    public CommonResponse<AccountDTO.Response> getAccount(@PathVariable Integer cardinal) {
        return CommonResponse.createSuccess(accountService.find(cardinal));
    }
}
