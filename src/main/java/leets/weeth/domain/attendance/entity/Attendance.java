package leets.weeth.domain.attendance.entity;

import jakarta.persistence.*;
import leets.weeth.domain.attendance.entity.enums.Week;
import leets.weeth.domain.user.entity.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Entity
@NoArgsConstructor
public class Attendance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long attendanceId;;

    private boolean isAttend;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Enumerated(EnumType.STRING)
    private Week week;

    @Builder
    public Attendance(Long attendanceId, User user, Week week) {
        this.attendanceId = attendanceId;
        this.isAttend = isAttend();
        this.user = user;
        this.week = week;
    }
}