package leets.weeth.domain.attendance.repository;

import leets.weeth.domain.attendance.entity.Week;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WeekRepository extends JpaRepository<Week, Long> {
    Optional<Week> findByAttendanceCode(String attendanceCode);
    Optional<Week> findByWeekNumber(int weekNumber);  //주차 번호를 기반으로 Week 엔티티를 조회
}
