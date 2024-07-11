package leets.weeth.domain.event.repository;

import leets.weeth.domain.event.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface EventRepository extends JpaRepository<Event, Long> {

    List<Event> findByStartDateTimeBetween(LocalDateTime startDate, LocalDateTime endDate);

    // 유저가 작성한 일정 리스트 반환. 필요하다면 사용
//    Optional<Event> findByIdAndUserId(Long eventId, Long userId);
}
