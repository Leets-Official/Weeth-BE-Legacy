package leets.weeth.domain.event.service;

import jakarta.persistence.EntityNotFoundException;
import leets.weeth.domain.calendar.entity.Calendar;
import leets.weeth.domain.calendar.entity.EventCalendar;
import leets.weeth.domain.calendar.repository.EventCalendarRepository;
import leets.weeth.domain.calendar.service.CalendarService;
import leets.weeth.domain.event.dto.RequestEvent;
import leets.weeth.domain.event.dto.ResponseEvent;
import leets.weeth.domain.event.entity.Event;
import leets.weeth.domain.event.mapper.EventMapper;
import leets.weeth.domain.event.repository.EventRepository;
import leets.weeth.domain.user.entity.User;
import leets.weeth.domain.user.repository.UserRepository;
import leets.weeth.global.common.error.exception.custom.BusinessLogicException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static leets.weeth.domain.event.entity.enums.ErrorMessage.*;


@Service
@RequiredArgsConstructor
public class EventService {
    private final EventRepository eventRepository;

    private final UserRepository userRepository;

    private final EventCalendarRepository eventCalendarRepository;

    private final CalendarService calendarService;

    private final EventMapper eventMapper;

    // 일정 생성
    @Transactional
    public void createEvent(RequestEvent requestEvent, Long userId) throws BusinessLogicException {
        // 기간 입력 검증
        LocalDateTime start = requestEvent.startDateTime();
        LocalDateTime end = requestEvent.endDateTime();
        validateDateRange(start, end);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(USER_NOT_FOUND.getMessage()));
        Event event = Event.fromDto(requestEvent, user);
        eventRepository.save(event);
        addEventToCalendar(event, start, end);
    }

    // 일정 상세 조회
    @Transactional(readOnly = true)
    public ResponseEvent getEventById(Long id) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(EVENT_NOT_FOUND.getMessage()));
        return eventMapper.toDto(event);
    }

    // 기간 별 일정 조회
    @Transactional(readOnly = true)
    public List<ResponseEvent> getEventsBetweenDate(LocalDateTime startDate, LocalDateTime endDate) throws BusinessLogicException {
        // 기간 입력 검증
        validateDateRange(startDate, endDate);

        List<Event> events = eventRepository.findByStartDateTimeBetween(startDate, endDate);
        return events.stream()
                .map(eventMapper::toDto)
                .toList();
    }

//    // 일정 수정 -> 현재 pr 피드백 후 수정할 예정
//    @Transactional
//    public void updateEvent(Long eventId, RequestEvent updatedEvent, Long userId) throws BusinessLogicException {
//        // 일정을 생성한 사용자인지 확인
//        Event oldEvent = validateEventOwner(eventId, userId);
//
//        // 기존 이벤트의 시작 및 종료 날짜
//        LocalDateTime oldStartDate = oldEvent.getStartDateTime();
//        LocalDateTime oldEndDate = oldEvent.getEndDateTime();
//
//        // 업데이트된 이벤트의 시작 및 종료 날짜
//        LocalDateTime updatedStartDate = updatedEvent.startDateTime();
//        LocalDateTime updatedEndDate = updatedEvent.endDateTime();
//
//        // 기존 이벤트와 새로운 이벤트의 달 정보가 다른지 확인
//        boolean dateChanged = !oldStartDate.toLocalDate().equals(updatedStartDate.toLocalDate()) || !oldEndDate.toLocalDate().equals(updatedEndDate.toLocalDate());
//
//        // 이벤트 업데이트
//        oldEvent.updateFromDto(updatedEvent);
//
//        // 날짜 정보가 변경되었을 경우, EventCalendar 업데이트
//        if (dateChanged) {
//            // 기존 EventCalendar 업데이트
//            updateEventCalendars(oldEvent, updatedStartDate, updatedEndDate);
//        }
//    }
//
//    private void updateEventCalendars(Event event, LocalDateTime start, LocalDateTime end) {
//        List<EventCalendar> eventCalendars = eventCalendarRepository.findByEventId(event.getId());
//
//        // 기존 EventCalendar 엔티티들을 순회하며 업데이트
//        for (EventCalendar eventCalendar : eventCalendars) {
//            Calendar calendar = calendarService.getCalendar(start.getYear(), start.getMonthValue());
//            eventCalendar.updateCalendar(calendar);
//            eventCalendarRepository.save(eventCalendar);
//
//            start = start.plusMonths(1).withDayOfMonth(1);
//            if (start.isAfter(end)) {
//                break;
//            }
//        }
//
//        // 남은 날짜에 대해 새로운 EventCalendar 엔티티 생성
//            addEventToCalendar(event, start, end);
//
//        // 날짜가 많았다가 적어진 경우를 체크해서 eventCalendar를 삭제
//
//    }

    // 일정 삭제
    @Transactional
    public void deleteEvent(Long eventId, Long userId) throws BusinessLogicException {
        // 일정을 생성한 사용자인지 확인
        Event oldEvent = validateEventOwner(eventId, userId);
        // 중간 테이블인 eventCalendar의 데이터 먼저 삭제
        eventCalendarRepository.deleteByEventId(eventId);
        eventRepository.deleteById(eventId);
    }

    // 해당 일정을 생성한 사용자와 같은지 검증
    private Event validateEventOwner(Long eventId, Long userId) throws BusinessLogicException {
        Event oldEvent = eventRepository.findById(eventId)
                .orElseThrow(() -> new EntityNotFoundException(EVENT_NOT_FOUND.getMessage()));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(USER_NOT_FOUND.getMessage()));

        // 일정을 생성한 사용자와 같은지 확인
        if(!user.getId().equals(oldEvent.getUser().getId())){
            throw new BusinessLogicException(USER_NOT_MATCH.getMessage());
        }
        return oldEvent;
    }

    // 시작 날짜가 종료 날짜 보다 느린지 검증
    private void validateDateRange(LocalDateTime start, LocalDateTime end) throws BusinessLogicException {
        if (start.isAfter(end)) {
            throw new BusinessLogicException(INVALID_DATE.getMessage());
        }
    }

    // 일정이 저장될 때 달 정보를 매핑하기 위해 캘린더에 저장
    private void addEventToCalendar(Event event, LocalDateTime start, LocalDateTime end) {
        while(!start.isAfter(end)) {
            Calendar calendar = calendarService.getCalendar(start.getYear(), start.getMonthValue());
            EventCalendar eventCalendar = EventCalendar.builder()
                    .event(event)
                    .calendar(calendar)
                    .build();
            eventCalendarRepository.save(eventCalendar);
            start = start.plusMonths(1).withDayOfMonth(1);
        }
    }
}
