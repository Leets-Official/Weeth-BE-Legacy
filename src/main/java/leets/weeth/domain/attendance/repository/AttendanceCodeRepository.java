package leets.weeth.domain.attendance.repository;


import leets.weeth.domain.attendance.entity.AttendanceCode;
import leets.weeth.domain.attendance.entity.enums.Week;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

public interface AttendanceCodeRepository extends JpaRepository<AttendanceCode, Long> {
    Optional<AttendanceCode> findByWeekAndDate(Week week, LocalDate date);
}
