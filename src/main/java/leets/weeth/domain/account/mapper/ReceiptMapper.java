package leets.weeth.domain.account.mapper;

import leets.weeth.domain.account.dto.ReceiptDTO;
import leets.weeth.domain.account.entity.Account;
import leets.weeth.domain.account.entity.Receipt;
import leets.weeth.domain.file.entity.File;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ReceiptMapper {

    @Mapping(target = "images", expression = "java( toUrls(receipt.getImages()) )")
    ReceiptDTO.Response to(Receipt receipt);

    @Mapping(target = "description", source = "dto.description")
    @Mapping(target = "account", source = "account")
    Receipt from(ReceiptDTO.Spend dto, Account account, List<File> images);

    default List<String> toUrls(List<File> images) {
        return images.stream()
                .map(File::getUrl)
                .toList();
    }
}