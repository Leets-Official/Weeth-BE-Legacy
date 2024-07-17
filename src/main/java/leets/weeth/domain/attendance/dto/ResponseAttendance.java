package leets.weeth.domain.attendance.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ResponseAttendance {
    private String scheduleTitle; // 일정 제목
    private String scheduleDateTime; // 날짜, 시간
    private String scheduleLocation; // 장소
    private double attendanceRate; // 출석률
    private double absenceRate; // 결석률
    private String weekStartDate; // 주차 시작 날짜
    private String weekEndDate; // 주차 끝나는 날짜

    public static ResponseAttendance create(
            String title,
            String dateTime,
            String location,
            double attendanceRate,
            double absenceRate,
            String weekStartDate,
            String weekEndDate) {
        return ResponseAttendance.builder()
                .scheduleTitle(title)
                .scheduleDateTime(dateTime)
                .scheduleLocation(location)
                .attendanceRate(attendanceRate)
                .absenceRate(absenceRate)
                .weekStartDate(weekStartDate)
                .weekEndDate(weekEndDate)
                .build();
    }
}
