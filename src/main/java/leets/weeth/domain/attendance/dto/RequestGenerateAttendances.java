package leets.weeth.domain.attendance.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RequestGenerateAttendances {
    @NotNull(message = "주차 수를 입력해 주세요.")
    @Min(value = 1, message = "주차 수는 1 이상이어야 합니다.")
    private Integer weeks;

    @NotNull(message = "사용자 ID를 입력해 주세요.")
    private Long userId;
}
