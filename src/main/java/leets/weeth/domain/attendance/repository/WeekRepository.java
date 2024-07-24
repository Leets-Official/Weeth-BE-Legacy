package leets.weeth.domain.attendance.repository;

import leets.weeth.domain.attendance.entity.Week;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface WeekRepository extends JpaRepository<Week, Long> {
    List<Week> findAllByCardinal(int cardinal);
    Optional<Week> findByDate(LocalDate date);
}
