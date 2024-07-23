package leets.weeth.domain.account.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public class AccountDTO {

    public record Response(
            Long id,
            String description,
            Integer total,
            Integer cardinal,
            List<ReceiptDTO.Response> receipts
    ) {}

    public record Save(
            String description,
            @NotNull Integer total,
            @NotNull Integer cardinal
    ) {}
}
