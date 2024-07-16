package leets.weeth.domain.calendar.service;

import jakarta.persistence.EntityNotFoundException;
import leets.weeth.domain.calendar.dto.ResponseMonthCalendar;
import leets.weeth.domain.calendar.dto.ResponseYearCalendar;
import leets.weeth.domain.calendar.entity.Calendar;
import leets.weeth.domain.calendar.repository.CalendarRepository;
import leets.weeth.domain.event.dto.ResponseEvent;
import leets.weeth.domain.event.entity.Event;
import leets.weeth.domain.event.mapper.EventMapper;
import leets.weeth.domain.event.repository.EventRepository;
import leets.weeth.global.common.error.exception.custom.BusinessLogicException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CalendarService {
    private final CalendarRepository calendarRepository;
    private final EventRepository eventRepository;
    private final EventMapper eventMapper;

    // 캘린더 초기화
    public void initCalendar(){

    }
    // 캘린더 조회, 없다면 생성, eventService, noticeService에서 사용
    public Calendar getCalendar(int year, int month){
        return calendarRepository.findByYearAndMonth(year, month).orElseGet(()-> {
            return calendarRepository.save(Calendar.fromDate(year, month));
        });
    }
    // 캘린더 조회 (월별)
    public ResponseMonthCalendar getData(int year, int month){
        Calendar calendar = calendarRepository.findByYearAndMonth(year, month).orElseThrow(()-> new EntityNotFoundException("캘린더가 존재하지 않습니다."));
        List<Event> events = eventRepository.findByCalendarId(calendar.getId());
        return new ResponseMonthCalendar(year, month, events.stream()
                    .map(eventMapper::toDto)
                    .toList());
    }

    // 캘린더 조회 (년별)
    public ResponseYearCalendar getDataYear(int year) throws BusinessLogicException {
        // 러프하게 년도 시작과 끝에서 events와 notices를 조회하고 정렬해서 DTO에 담아서 반환
        LocalDateTime start = LocalDateTime.of(year, 1, 1, 0, 0);
        LocalDateTime end = LocalDateTime.of(year, 12, 31, 23, 59);
        List<Event> events = eventRepository.findByStartDateTimeBetween(start, end);

        Map<Integer, List<ResponseEvent>> eventsByMonth = events.stream()
                .sorted((e1, e2) -> e1.getStartDateTime().compareTo(e2.getStartDateTime()))
                .map(eventMapper::toDto)
                .collect(Collectors.groupingBy(responseEvent -> responseEvent.startDateTime().getMonthValue()));

        // 공지사항도 created_at 날짜를 이용해 해당 년도의 공지를 가져와서 필요하면 정렬해서 보내주면 될 듯
        return new ResponseYearCalendar(year, eventsByMonth);

        // 해당 년도의 캘린더 테이블을 돌아 ID를 구해서 해당 ID와 매칭되는 events와 notices들을 조회해서 정렬해서 DTO에 담아서 반환 -> 좀 비효율 적인거 같은데

    }

}
