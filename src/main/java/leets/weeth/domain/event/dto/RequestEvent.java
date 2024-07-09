package leets.weeth.domain.event.dto;

import lombok.Getter;

import java.time.LocalDateTime;

public record RequestEvent (
     String title,
     String content,
     String location,
     LocalDateTime startDateTime,
     LocalDateTime endDateTime
) {}
