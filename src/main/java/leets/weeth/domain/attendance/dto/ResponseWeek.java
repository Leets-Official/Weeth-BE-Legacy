package leets.weeth.domain.attendance.dto;

import lombok.*;
import leets.weeth.domain.attendance.entity.enums.Week;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResponseWeek {
    private Long id;
    private String attendanceCode;
    private LocalDateTime expirationTime;
    private Week week;
}