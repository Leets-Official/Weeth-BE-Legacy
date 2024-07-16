package leets.weeth.domain.event.repository;

import leets.weeth.domain.event.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface EventRepository extends JpaRepository<Event, Long> {

    List<Event> findByStartDateTimeBetween(LocalDateTime startDate, LocalDateTime endDate);

    @Query("SELECT e FROM Event e JOIN FETCH e.user JOIN e.eventCalendars ec WHERE ec.calendar.id = :calendarId")
    List<Event> findByCalendarId(@Param("calendarId") Long calendarId);

    // 좀 더 알아보고 쿼리 최적화하기
    @Query("SELECT e FROM Event e JOIN FETCH e.user WHERE YEAR(e.startDateTime) = :year")
    List<Event> findAllByYear(@Param("year") int year);

    // 유저가 작성한 일정 리스트 반환. 필요하다면 사용
//    Optional<Event> findByIdAndUserId(Long eventId, Long userId);
}
