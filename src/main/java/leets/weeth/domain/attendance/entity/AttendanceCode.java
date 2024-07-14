package leets.weeth.domain.attendance.entity;

import jakarta.persistence.*;
import leets.weeth.domain.attendance.entity.enums.Week;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class AttendanceCode {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long AttendanceCode_id;

    @Column(nullable = false)
    private String attendanceCode;

    @Column(nullable = false)
    private LocalDateTime expirationTime;

    @Column(nullable = false)
    private Week week;

    @Column(name = "date")
    private LocalDate date;
}
