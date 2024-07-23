package leets.weeth.domain.attendance.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ResponseAttendance {
    private String scheduleTitle;
    private LocalDateTime scheduleDateTime;
    private String scheduleLocation;
    private int attendanceRate;
    private LocalDateTime attendanceDate;
}

