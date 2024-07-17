package leets.weeth.domain.attendance.repository;

import leets.weeth.domain.attendance.entity.Attendance;
import leets.weeth.domain.attendance.entity.enums.WeekEnum;
import leets.weeth.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
    Optional<Attendance> findByUserAndWeek(User user, WeekEnum WeekEnum);
    List<Attendance> findByUser(User user);
    long countByUserAndIsAttendTrue(User user);
    long countByUserAndIsAttendFalse(User user);
}
