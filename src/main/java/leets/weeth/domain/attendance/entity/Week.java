package leets.weeth.domain.attendance.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


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

    @Builder
    public Week(String attendanceCode, String weekInfo, int cardinal) {
        this.attendanceCode = attendanceCode;
        this.weekInfo = weekInfo;
        this.cardinal = cardinal;
    }
}
