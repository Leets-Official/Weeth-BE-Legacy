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
        LocalDateTime startDateTime,
        LocalDateTime endDateTime,
        LocalDateTime created_at,
        LocalDateTime modified_at,
        String userName,
        Type type
) {}
