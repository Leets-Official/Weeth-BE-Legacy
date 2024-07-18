package leets.weeth.domain.notice.dto;

import leets.weeth.domain.event.entity.enums.Type;
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
        Type type
        // 파일관련

) {
}
