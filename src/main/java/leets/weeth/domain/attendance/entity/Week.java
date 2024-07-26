package leets.weeth.domain.attendance.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import leets.weeth.domain.attendance.dto.RequestAttendance;
import leets.weeth.global.common.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;


@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Week extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long weekId;

    private String attendanceCode;

    private String weekInfo;

    private Integer cardinal;

    private Integer weekNumber;

    private LocalDate date;

    public boolean isNotMatch(RequestAttendance dto) {
        return !this.attendanceCode.equals(dto.getAttendanceCode());
    }
}
