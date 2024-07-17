package leets.weeth.domain.attendance.entity;

import jakarta.persistence.*;
import leets.weeth.domain.attendance.entity.enums.WeekEnum;
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

    @Enumerated(EnumType.STRING)
    private WeekEnum week;

    @Builder
    public AttendanceCode(Long attendanceCodeId, String attendanceCode, LocalDateTime expirationTime, WeekEnum week) {
        this.attendanceCodeId = attendanceCodeId;
        this.attendanceCode = attendanceCode;
        this.expirationTime = expirationTime;
        this.week = week;
    }
}
