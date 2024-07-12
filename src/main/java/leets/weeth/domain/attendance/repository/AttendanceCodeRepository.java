package leets.weeth.domain.attendance.repository;


import leets.weeth.domain.attendance.entity.AttendanceCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AttendanceCodeRepository extends JpaRepository<AttendanceCode, Long> {
}
