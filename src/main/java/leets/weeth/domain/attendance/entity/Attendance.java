package leets.weeth.domain.attendance.entity;

import jakarta.persistence.*;
import leets.weeth.domain.user.entity.User;
import leets.weeth.global.common.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Attendance extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long attendanceId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private Boolean isAttend;

    @ManyToOne
    @JoinColumn(name = "week_id")
    private Week week;

    private LocalDateTime attendanceDateTime;

    public void attend(boolean isAttend) {
        this.isAttend = isAttend;
        this.attendanceDateTime = LocalDateTime.now(); //출석시간은 현재 시간을 반환하도록 설정
    }
}
