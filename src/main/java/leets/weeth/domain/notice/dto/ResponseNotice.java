package leets.weeth.domain.notice.dto;

import leets.weeth.domain.event.entity.enums.Type;
import leets.weeth.domain.file.entity.File;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record ResponseNotice(
        Long id,
        String title,
        String content,
        LocalDateTime createdAt,
        LocalDateTime modifiedAt,
        String userName,
        Type type,
        List<File> fileUrls
) {
}
