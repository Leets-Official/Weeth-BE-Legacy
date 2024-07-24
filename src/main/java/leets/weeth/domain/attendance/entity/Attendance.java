package leets.weeth.domain.attendance.entity;

import jakarta.persistence.*;
import leets.weeth.domain.user.entity.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Getter
@Entity
@NoArgsConstructor
public class Attendance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long attendanceId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private boolean isAttend;

    @ManyToOne
    @JoinColumn(name = "week_id")
    private Week week;

    private LocalDateTime attendanceDateTime;
    @Builder
    public Attendance(User user, boolean isAttend, Week week) {
        this.user = user;
        this.isAttend = isAttend;
        this.week = week;
    }
    public void setIsAttend(boolean isAttend) {
        this.isAttend = isAttend;
        this.attendanceDateTime = LocalDateTime.now(); //출석시간은 현재 시간을 반환하도록 설정
    }
}
