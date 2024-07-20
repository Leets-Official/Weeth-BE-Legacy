package leets.weeth.domain.event.dto;

import leets.weeth.domain.event.entity.enums.Type;
import lombok.Builder;

import java.time.LocalDateTime;


@Builder
public record ResponseEvent (
        Long id,
        String title,
        String content,
        String location,
        String requiredItems,
        String memberNumber,
        LocalDateTime startDateTime,
        LocalDateTime endDateTime,
        LocalDateTime createdAt,
        LocalDateTime modifiedAt,
        String userName,
        Type type
) {}
