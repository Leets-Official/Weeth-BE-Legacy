package leets.weeth.domain.notice.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record RequestNotice(
        @NotBlank String title,
        @NotBlank String content
) {
}
