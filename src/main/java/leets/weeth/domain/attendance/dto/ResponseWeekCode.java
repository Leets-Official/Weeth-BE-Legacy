package leets.weeth.domain.attendance.dto;

import leets.weeth.domain.attendance.entity.Week;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ResponseWeekCode {
    private int weekNumber;
    private String attendanceCode;
    private LocalDate localDate;

    public ResponseWeekCode(Week week) {
        this.weekNumber = week.getWeekNumber();
        this.attendanceCode = week.getAttendanceCode();
        this.localDate = week.getDate();
    }
}
