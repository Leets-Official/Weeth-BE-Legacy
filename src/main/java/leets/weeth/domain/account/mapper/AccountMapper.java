package leets.weeth.domain.account.mapper;

import leets.weeth.domain.account.dto.AccountDTO;
import leets.weeth.domain.account.dto.ReceiptDTO;
import leets.weeth.domain.account.entity.Account;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AccountMapper {

    @Mapping(target = "receipts", expression = "java( mapToReceiptDTO(account, receiptMapper) )")
    AccountDTO.Response to(Account account, @Context ReceiptMapper receiptMapper);

    Account from(AccountDTO.Save dto);

    @Named("mapToReceiptDTO")
    default List<ReceiptDTO.Response> mapToReceiptDTO(Account account, ReceiptMapper receiptMapper) {
        return account.getReceipts().stream()
                .map(receiptMapper::to)
                .toList();
    }
}

