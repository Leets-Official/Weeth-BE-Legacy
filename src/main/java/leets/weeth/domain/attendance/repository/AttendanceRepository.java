package leets.weeth.domain.attendance.repository;

import leets.weeth.domain.attendance.entity.Attendance;
import leets.weeth.domain.attendance.entity.enums.Week;
import leets.weeth.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
    Optional<Attendance> findByUserAndWeek(User user, Week week);
    long countByUserAndIsAttendTrue(User user);
}
