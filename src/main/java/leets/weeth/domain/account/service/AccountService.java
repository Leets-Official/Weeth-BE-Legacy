package leets.weeth.domain.account.service;

import leets.weeth.domain.account.dto.AccountDTO;
import leets.weeth.domain.account.entity.Account;
import leets.weeth.domain.account.mapper.AccountMapper;
import leets.weeth.domain.account.mapper.ReceiptMapper;
import leets.weeth.domain.account.repository.AccountRepository;
import leets.weeth.global.common.error.exception.custom.AccountExistsException;
import leets.weeth.global.common.error.exception.custom.AccountNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;
    private final ReceiptMapper receiptMapper;

    public AccountDTO.Response find(Integer cardinal) {
        return accountRepository.findByCardinal(cardinal)
                .map(account -> accountMapper.to(account, receiptMapper))
                .orElseThrow(AccountNotFoundException::new);
    }

    public void init(AccountDTO.Save dto) {
        if (accountRepository.findByCardinal(dto.cardinal()).isPresent())
            throw new AccountExistsException();

        Account account = accountMapper.from(dto);
        accountRepository.save(account);
    }
}
