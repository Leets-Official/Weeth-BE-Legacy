package leets.weeth.domain.event.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record RequestEvent (
     @NotBlank String title,
     @NotBlank String content,
     String location,
     @NotNull boolean isAllDay,
     @NotNull LocalDateTime startDateTime,
     @NotNull LocalDateTime endDateTime
) {}
