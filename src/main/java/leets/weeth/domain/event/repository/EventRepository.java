package leets.weeth.domain.event.repository;

import leets.weeth.domain.event.entity.Event;
import leets.weeth.domain.event.entity.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface EventRepository extends JpaRepository<Event, Long> {
    // 기간 내의 모든 일정 반환
    @Query("SELECT e FROM Event e JOIN FETCH e.user WHERE e.startDateTime BETWEEN :startDate AND :endDate OR e.endDateTime BETWEEN :startDate AND :endDate")
    List<Event> findByStartDateTimeBetween(LocalDateTime startDate, LocalDateTime endDate);

    // status에 맞는 일정 한 개 반환
    @Query("SELECT e FROM Event e JOIN FETCH e.user WHERE e.id = :id AND e.status = :status")
    Optional<Event> findByIdAndStatus(@Param("id") Long id, @Param("status") Status status);

    // status에 맞는 일정 모두 반환
    @Query("SELECT e FROM Event e JOIN FETCH e.user WHERE e.status = :status")
    List<Event> findAllByStatus(@Param("status") Status status);
}
