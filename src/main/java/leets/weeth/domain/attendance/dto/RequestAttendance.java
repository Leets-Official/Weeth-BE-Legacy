package leets.weeth.domain.attendance.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestAttendance {
    @NotBlank
    private Long attendanceCodeId;
    @Getter
    private String attendanceCode;
    @NotNull
    private Long userId;
    @NotNull
    private boolean isAttend;

}
