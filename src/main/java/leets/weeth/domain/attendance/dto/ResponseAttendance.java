package leets.weeth.domain.attendance.dto;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public class ResponseAttendance {
    private String scheduleTitle;
    private String scheduleDateTime;
    private String scheduleLocation;
    private int attendanceRate;
    private LocalDateTime attendanceDate;
}

