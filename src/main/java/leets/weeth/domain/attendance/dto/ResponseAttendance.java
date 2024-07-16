package leets.weeth.domain.attendance.dto;

import lombok.*;

@Data
public class ResponseAttendance {
    private String scheduleTitle; //일정 제목
    private String scheduleDateTime; //날짜, 시간
    private String scheduleLocation; //장소
    private double attendanceRate; //출석률
    private double absenceRate; //결석률
}
