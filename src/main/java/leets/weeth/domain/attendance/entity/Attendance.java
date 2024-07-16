package leets.weeth.domain.attendance.entity;

import jakarta.persistence.*;
import leets.weeth.domain.attendance.entity.enums.Week;
import leets.weeth.domain.user.entity.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Attendance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long attendanceId;;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private boolean isAttend;
    private String attendanceCode;
    @Enumerated(EnumType.STRING)
    private Week week;
}