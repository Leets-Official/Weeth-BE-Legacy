package leets.weeth.domain.attendance.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WeekDto {
    private Long id;
    private String attendanceCode;
    private LocalDateTime expirationTime;
    private int week;
}
