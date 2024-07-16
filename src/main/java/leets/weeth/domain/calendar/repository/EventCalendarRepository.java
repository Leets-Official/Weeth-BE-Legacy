package leets.weeth.domain.calendar.repository;

import leets.weeth.domain.calendar.entity.EventCalendar;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventCalendarRepository extends JpaRepository<EventCalendar, Long> {
}
