package leets.weeth.domain.attendance.dto;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ResponseAttendanceDetails {
    private Long attendanceId;
    private long totalAttendanceCount;
    private long totalAbsenceCount;
    private long totalMeetingsCount;
}