package leets.weeth.domain.calendar.repository;

import leets.weeth.domain.calendar.entity.EventCalendar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EventCalendarRepository extends JpaRepository<EventCalendar, Long> {

    @Query("SELECT ec FROM EventCalendar ec JOIN FETCH ec.event e JOIN FETCH e.user WHERE ec.calendar.id IN :calendarIds")
    List<EventCalendar> findByCalendarIds(@Param("calendarIds") List<Long> calendarIds);

    @Query("SELECT ec FROM EventCalendar ec JOIN FETCH ec.event e JOIN FETCH e.user WHERE ec.event.id= :eventId")
    List<EventCalendar> findByEventId(@Param("eventId") Long eventId);

    void deleteByEventId(Long eventId);
}
