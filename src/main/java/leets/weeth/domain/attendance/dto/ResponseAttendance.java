package leets.weeth.domain.attendance.dto;

import lombok.*;
@Getter
@Builder
public class ResponseAttendance {
    private String scheduleTitle; //일정 제목
    private String scheduleDateTime; //날짜, 시간
    private String scheduleLocation; //장소
    private double attendanceRate; //출석률
    private double absenceRate; //결석률

    public static ResponseAttendance create(String scheduleTitle, String scheduleDateTime, String scheduleLocation, double attendanceRate, double absenceRate) {
        return ResponseAttendance.builder()
                .scheduleTitle(scheduleTitle)
                .scheduleDateTime(scheduleDateTime)
                .scheduleLocation(scheduleLocation)
                .attendanceRate(attendanceRate)
                .absenceRate(absenceRate)
                .build();
    }
}
