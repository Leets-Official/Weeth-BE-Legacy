package leets.weeth.domain.attendance.repository;

import leets.weeth.domain.attendance.entity.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
    List<Integer> findDistinctWeeks();
    long countByUserIdAndIsAttendTrue(Long userId);
//    Long countByUserIdAndAttendIsTrue(Long userId);
}