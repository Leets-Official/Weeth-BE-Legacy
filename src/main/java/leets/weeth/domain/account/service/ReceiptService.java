package leets.weeth.domain.account.service;

import leets.weeth.domain.account.dto.ReceiptDTO;
import leets.weeth.domain.account.entity.Account;
import leets.weeth.domain.account.entity.Receipt;
import leets.weeth.domain.account.mapper.ReceiptMapper;
import leets.weeth.domain.account.repository.AccountRepository;
import leets.weeth.domain.account.repository.ReceiptRepository;
import leets.weeth.domain.file.entity.File;
import leets.weeth.domain.file.service.FileService;
import leets.weeth.global.common.error.exception.custom.AccountNotFoundException;
import leets.weeth.global.common.error.exception.custom.ReceiptNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReceiptService {

    private final ReceiptRepository receiptRepository;
    private final AccountRepository accountRepository;
    private final FileService fileService;
    private final ReceiptMapper mapper;

    @Transactional
    public void spend(ReceiptDTO.Spend dto, Integer cardinal, List<MultipartFile> files) {
        Account account = accountRepository.findByCardinal(cardinal)
                .orElseThrow(AccountNotFoundException::new);

        List<File> images = fileService.uploadFiles(files);

        Receipt receipt = mapper.from(dto, account, images);
        receiptRepository.save(receipt);
        account.spend(receipt);
    }

    @Transactional
    public void cancel(Long receiptId) {
        Receipt receipt = receiptRepository.findById(receiptId)
                .orElseThrow(ReceiptNotFoundException::new);

        receiptRepository.delete(receipt);
        receipt.getAccount().cancel(receipt);
    }
}
