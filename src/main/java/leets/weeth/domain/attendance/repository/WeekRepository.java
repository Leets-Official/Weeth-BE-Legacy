package leets.weeth.domain.attendance.repository;

import leets.weeth.domain.attendance.entity.Week;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface WeekRepository extends JpaRepository<Week, Long> {
    Optional<Week> findByAttendanceCode(String attendanceCode);
}
