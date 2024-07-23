package leets.weeth.domain.account.dto;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public class ReceiptDTO {

    public record Response(
            Long id,
            Integer amount,
            String description,
            String imageUrl,
            LocalDate date
    ) {}

    public record Spend(
            @NotNull Integer amount,
            String description,
            String imageUrl,
            LocalDate date
    ) {}
}
