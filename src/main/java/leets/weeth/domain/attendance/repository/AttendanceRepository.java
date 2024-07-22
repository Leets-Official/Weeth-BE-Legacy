package leets.weeth.domain.attendance.repository;

import leets.weeth.domain.attendance.entity.Attendance;
import leets.weeth.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
    List<Attendance> findAllByUserAndWeek(User user, int week);
    long countByUserAndIsAttendTrue(User user);
}
