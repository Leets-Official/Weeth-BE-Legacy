package leets.weeth.domain.attendance.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor
public class Week {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long weekId;

    private String attendanceCode;

    private String weekInfo;

    private int cardinal;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;

    @Builder
    public Week(String attendanceCode, String weekInfo, int cardinal, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        this.attendanceCode = attendanceCode;
        this.weekInfo = weekInfo;
        this.cardinal = cardinal;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
    }
}
