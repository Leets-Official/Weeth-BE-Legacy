package leets.weeth.domain.account.dto;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.List;

public class ReceiptDTO {

    public record Response(
            Long id,
            Integer amount,
            String description,
            List<String> images,
            LocalDate date
    ) {}

    public record Spend(
            @NotNull Integer amount,
            String description,
            LocalDate date
    ) {}
}
