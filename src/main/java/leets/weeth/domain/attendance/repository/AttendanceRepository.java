package leets.weeth.domain.attendance.repository;

import leets.weeth.domain.attendance.entity.Attendance;
import leets.weeth.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import leets.weeth.domain.attendance.entity.Week;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
    Optional<Attendance> findAllByUserAndWeek(User user, Week week);
    //특정 사용자의 출석 정보 중에서 출석한 횟수를 카운트
    @Query("SELECT COUNT(a) FROM Attendance a WHERE a.user = :user AND a.isAttend = true")
    long countByUserAndIsAttendTrue(@Param("user") User user);
}
