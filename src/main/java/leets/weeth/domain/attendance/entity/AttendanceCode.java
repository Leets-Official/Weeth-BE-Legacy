package leets.weeth.domain.attendance.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor
public class AttendanceCode {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long attendanceCodeId;

    private String attendanceCode;
    private LocalDateTime expirationTime;

    @Builder
    public AttendanceCode(Long attendanceCodeId, String attendanceCode, LocalDateTime expirationTime) {
        this.attendanceCodeId = attendanceCodeId;
        this.attendanceCode = attendanceCode;
        this.expirationTime = expirationTime;
    }
}
