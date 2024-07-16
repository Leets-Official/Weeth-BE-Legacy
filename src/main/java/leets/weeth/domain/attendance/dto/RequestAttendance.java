package leets.weeth.domain.attendance.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RequestAttendance {
    @NotBlank(message = "출석 코드는 필수 입력 항목입니다.")
    private String attendanceCode;
}
