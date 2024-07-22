package leets.weeth.domain.attendance.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RequestAttendance {
    @NotBlank(message = "출석 코드를 입력해주세요.")
    private String attendanceCode;
}
