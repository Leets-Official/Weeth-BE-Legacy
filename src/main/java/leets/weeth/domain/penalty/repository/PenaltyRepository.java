package leets.weeth.domain.penalty.repository;

import leets.weeth.domain.penalty.entity.Penalty;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PenaltyRepository extends JpaRepository<Penalty, Long> {
    List<Penalty> findByUserId(Long userId, Sort sort);
}
