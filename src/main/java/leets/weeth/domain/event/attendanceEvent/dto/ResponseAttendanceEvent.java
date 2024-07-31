package leets.weeth.domain.event.attendanceEvent.dto;

import leets.weeth.domain.attendance.dto.ResponseWeekCode;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ResponseAttendanceEvent(
        String title,
        String content,
        String location,
        String requiredItems,
        String memberNumber,
        LocalDateTime startDateTime,
        LocalDateTime endDateTime,
        int cardinal,
        ResponseWeekCode weekCode
) {
}
