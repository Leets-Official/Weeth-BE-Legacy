package leets.weeth.domain.admin.attendanceEvent.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record RequestAttendanceEvent(
        @NotBlank String title,
        @NotBlank String content,
        @NotNull LocalDateTime startDateTime,
        @NotNull LocalDateTime endDateTime
) {
}
