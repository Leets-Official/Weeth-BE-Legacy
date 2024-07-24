package leets.weeth.domain.attendance.dto;
import lombok.Builder;
import lombok.Data;
@Data
@Builder
public class ResponseWeekCode {
    private int weekNumber;
    private String attendanceCode;
}
