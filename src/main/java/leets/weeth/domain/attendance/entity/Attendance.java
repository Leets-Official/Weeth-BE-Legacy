package leets.weeth.domain.attendance.entity;

import jakarta.persistence.*;
import leets.weeth.domain.user.entity.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Optional;

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

    private String attendanceCode;

    private boolean isAttend;

    private LocalDateTime startDateTime;

    private LocalDateTime endDateTime;

    private LocalDateTime attendanceDateTime;

    private int week;

    @Builder(toBuilder = true)
    public Attendance(Long attendanceId, User user, String attendanceCode, boolean isAttend,
                      LocalDateTime startDateTime, LocalDateTime endDateTime, LocalDateTime attendanceDateTime, int week) {
        this.attendanceId = attendanceId;
        this.user = user;
        this.attendanceCode = attendanceCode;
        this.isAttend = isAttend;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.attendanceDateTime = attendanceDateTime;
        this.week = week;
    }
}
