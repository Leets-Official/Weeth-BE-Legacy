package leets.weeth.domain.account.mapper;

import leets.weeth.domain.account.dto.ReceiptDTO;
import leets.weeth.domain.account.entity.Account;
import leets.weeth.domain.account.entity.Receipt;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ReceiptMapper {

    ReceiptDTO.Response to(Receipt receipt);

    @Mapping(target = "description", source = "dto.description")
    @Mapping(target = "account", source = "account")
    Receipt from(ReceiptDTO.Spend dto, Account account);
}