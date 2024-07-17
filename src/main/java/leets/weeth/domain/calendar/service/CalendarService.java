package leets.weeth.domain.calendar.service;

import jakarta.persistence.EntityNotFoundException;
import leets.weeth.domain.calendar.dto.ResponseMonthData;
import leets.weeth.domain.calendar.dto.ResponseYearData;
import leets.weeth.domain.calendar.entity.Calendar;
import leets.weeth.domain.calendar.entity.EventCalendar;
import leets.weeth.domain.calendar.repository.CalendarRepository;
import leets.weeth.domain.calendar.repository.EventCalendarRepository;
import leets.weeth.domain.event.dto.ResponseEvent;
import leets.weeth.domain.event.entity.Event;
import leets.weeth.domain.event.mapper.EventMapper;
import leets.weeth.domain.event.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CalendarService {
    private final CalendarRepository calendarRepository;
    private final EventCalendarRepository eventCalendarRepository;
    private final EventRepository eventRepository;
    private final EventMapper eventMapper;

    // 캘린더 조회, 없다면 생성, eventService, noticeService에서 사용
    @Transactional
    public Calendar getCalendar(int year, int month) {
        return calendarRepository.findByYearAndMonth(year, month).orElseGet(() -> {
            return calendarRepository.save(Calendar.fromDate(year, month));
        });
    }

    // 캘린더 조회 (월별)
    // 차후에 공지사항 데이터도 추가해서 반환예정
    @Transactional(readOnly = true)
    public ResponseMonthData getMonthDataByDate(int year, int month) {
        Calendar calendar = calendarRepository.findByYearAndMonth(year, month).orElseThrow(() -> new EntityNotFoundException("캘린더가 존재하지 않습니다."));
        List<Event> events = eventRepository.findByCalendarId(calendar.getId());
        return new ResponseMonthData(year, month, events.stream()
                .map(eventMapper::toDto)
                .toList());
    }

    // 캘린더 조회 (년별)
    // 차후에 공지사항 데이터도 추가해서 반환예정
    @Transactional(readOnly = true)
    public ResponseYearData getYearDataByDate(int year) {
        List<Calendar> calendars = calendarRepository.findByYear(year);
        List<Long> calendarIds = calendars.stream().map(Calendar::getId).toList();
        List<EventCalendar> eventCalendars = eventCalendarRepository.findByCalendarIds(calendarIds);

        Map<Integer, List<ResponseEvent>> responseData = eventCalendars.stream()
                .collect(Collectors.groupingBy(
                        eventCalendar -> eventCalendar.getCalendar().getMonth(),
                        Collectors.mapping(eventCalendar -> eventMapper.toDto(eventCalendar.getEvent()), Collectors.toList())));

        responseData.values().forEach(eventList ->
                eventList.sort((e1, e2) -> e1.startDateTime().compareTo(e2.startDateTime()))
        );
        // 캘린더 id -> 캘린더 달 정보를 토대로 그룹핑
        return new ResponseYearData(year, responseData);
    }
}
