package leets.weeth.domain.notice.dto;

import leets.weeth.domain.event.entity.enums.Status;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ResponseNotice(
        Long id,
        String title,
        String content,
        LocalDateTime created_at,
        LocalDateTime modified_at,
        String userName,
        Status status
        // 파일관련

) {
}
