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
import org.springframework.transaction.annotation.Transactional;

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

//    // 캘린더 조회 (년별)
//    public ResponseYearCalendar getDataYear(int year) throws BusinessLogicException {
//        //년도에 맞는 캘린더 반환, 해당 아이디로 이벤트_캘린더 조회 -> 이벤트 반환
//        List<Calendar> calendars = calendarRepository.findByYear(year);
//        List<Event> events = new ArrayList<>();
//        for(Long calendarId: calendars.stream().map(Calendar::getId).toList()){
//            List<Event> eventList = eventRepository.findByCalendarId(calendarId);
//            events.addAll(eventList);
//        }
//
//        Map<Integer, List<ResponseEvent>> eventsByMonth = events.stream()
//                .sorted((e1, e2) -> e1.getStartDateTime().compareTo(e2.getStartDateTime()))
//                .map(eventMapper::toDto)
//                .collect(Collectors.groupingBy(responseEvent -> responseEvent.startDateTime().getMonthValue()));
//        return new ResponseYearCalendar(year, eventsByMonth);
//    }

    @Transactional(readOnly = true)
    public ResponseYearCalendar getDataYear(int year) throws BusinessLogicException {
        List<Event> events = eventRepository.findAllByYear(year);

        Map<Integer, List<ResponseEvent>> eventsByMonth = events.stream()
                .sorted((e1, e2) -> e1.getStartDateTime().compareTo(e2.getStartDateTime()))
                .map(eventMapper::toDto)
                .collect(Collectors.groupingBy(responseEvent -> responseEvent.startDateTime().getMonthValue()));

        return new ResponseYearCalendar(year, eventsByMonth);
    }
}
