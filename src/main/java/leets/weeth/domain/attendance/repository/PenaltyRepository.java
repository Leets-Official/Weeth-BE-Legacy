package leets.weeth.domain.attendance.repository;

import leets.weeth.domain.attendance.entity.Penalty;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PenaltyRepository extends JpaRepository<Penalty, Long> {
    long countByUserId(Long userId);
}