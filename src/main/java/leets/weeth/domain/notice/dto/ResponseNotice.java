package leets.weeth.domain.notice.dto;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ResponseNotice(
        String title,
        String content,
        LocalDateTime created_at,
        LocalDateTime modified_at
        // 파일관련

) {
}
