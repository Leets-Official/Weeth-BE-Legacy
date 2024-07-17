package leets.weeth.domain.attendance.entity;

import jakarta.persistence.*;
import leets.weeth.domain.attendance.entity.enums.WeekEnum;
import leets.weeth.domain.user.entity.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
public class Attendance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long attendanceId;

    @Column(name = "is_attend")
    private boolean isAttend;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Enumerated(EnumType.STRING)
    private WeekEnum week;

    @Column(name = "attendance_code")
    private String attendanceCode;

    @Builder
    public Attendance(Long attendanceId, User user, String attendanceCode, boolean isAttend, WeekEnum week) {
        this.attendanceId = attendanceId;
        this.user = user;
        this.attendanceCode = attendanceCode;
        this.isAttend = isAttend;
        this.week = week;
    }
}
