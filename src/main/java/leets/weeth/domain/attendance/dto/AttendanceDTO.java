package leets.weeth.domain.attendance.dto;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public record AttendanceDTO() {

    public record Response(
            Long attendanceId,
            Boolean isAttend,
            Integer weekNumber,

            String title,
            LocalDateTime startDateTime,
            LocalDateTime endDateTime,
            String location
    ) {}

    public record Detail(
            Integer attendanceCount,
            Integer total,
            Integer absenceCount,
            List<Response> attendances
    ) {}

    public record Main(
            Integer attendanceRate,

            String title,
            LocalDateTime startDatetime,
            LocalDateTime endDateTime,
            String location
    ) {}

    public record Week(
            @NotNull Integer cardinal,
            @NotNull Integer weekNumber,
            @NotNull LocalDate date
    ) {}
}
