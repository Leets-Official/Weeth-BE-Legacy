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

    private boolean isAttend;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private String attendanceCode;

    private LocalDateTime startDateTime;

    private LocalDateTime endDateTime;

    private LocalDateTime attendanceDateTime;

    @Builder
    public Attendance(Long attendanceId, User user, String attendanceCode, boolean isAttend,
                      LocalDateTime startDateTime, LocalDateTime endDateTime, LocalDateTime attendanceDateTime) {
        this.attendanceId = attendanceId;
        this.user = user;
        this.attendanceCode = attendanceCode;
        this.isAttend = isAttend;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.attendanceDateTime = attendanceDateTime;
    }
}
