package leets.weeth.domain.attendance.dto;

import leets.weeth.domain.attendance.entity.Week;
import lombok.Data;

@Data
public class ResponseWeekCode {
    private int weekNumber;
    private String attendanceCode;

    public ResponseWeekCode(Week week) {
        this.weekNumber = week.getWeekNumber();
        this.attendanceCode = week.getAttendanceCode();
    }
}
