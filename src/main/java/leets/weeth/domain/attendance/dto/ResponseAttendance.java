package leets.weeth.domain.attendance.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResponseAttendance {
    @NotNull
    private Long id;
    @NotNull
    private Long attendanceCodeId;
    @NotNull
    private boolean isAttend;
    @NotNull
    private Long userId;
}
