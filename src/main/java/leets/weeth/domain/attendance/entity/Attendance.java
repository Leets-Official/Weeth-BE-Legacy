package leets.weeth.domain.attendance.entity;

import jakarta.persistence.*;
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

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private boolean isAttend;

    @ManyToOne
    @JoinColumn(name = "week_id")
    private Week week;

    @Builder
    public Attendance(User user, boolean isAttend, Week week) {
        this.user = user;
        this.isAttend = isAttend;
        this.week = week;
    }
    public void setIsAttend(boolean isAttend) {
        this.isAttend = isAttend;
    }
}
