package leets.weeth.domain.attendance.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ResponseAttendance {
    private String scheduleTitle;
    private String scheduleDateTime;
    private String scheduleLocation;
    private int attendanceRate;
    private LocalDateTime attendanceDate;
}

