package leets.weeth.domain.attendance.repository;

import leets.weeth.domain.attendance.entity.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
    @Query("SELECT DISTINCT a.week FROM Attendance a")
    List<Integer> findDistinctWeeks();

    long countByUserIdAndAttendTrue(Long userId);
}