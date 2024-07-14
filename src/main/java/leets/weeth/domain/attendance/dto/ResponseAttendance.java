package leets.weeth.domain.attendance.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResponseAttendance {
    private Long id;
    private Long attendanceCodeId;
    private boolean isAttend;
    private Long userId;
}
