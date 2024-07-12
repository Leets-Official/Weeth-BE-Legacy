package leets.weeth.domain.attendance.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import leets.weeth.domain.attendance.entity.enums.Week;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RequestWeek {
    private Long id;
    private String attendanceCode;
    private LocalDateTime expirationTime;
    private Week week;
}
