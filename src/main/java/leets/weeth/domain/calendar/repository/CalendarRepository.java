package leets.weeth.domain.calendar.repository;

import leets.weeth.domain.calendar.entity.Calendar;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CalendarRepository extends JpaRepository<Calendar, Long> {
    Optional<Calendar> findByYearAndMonth(int year, int month);

    List<Calendar> findByYear(int year);
}
