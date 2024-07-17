package leets.weeth.domain.attendance.repository;

import leets.weeth.domain.attendance.entity.AttendanceCode;
import leets.weeth.domain.attendance.entity.enums.WeekEnum;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface AttendanceCodeRepository extends JpaRepository<AttendanceCode, Long> {
    Optional<AttendanceCode> findByWeekAndExpirationTimeAfter(WeekEnum weekEnum, LocalDateTime currentTime);
}

