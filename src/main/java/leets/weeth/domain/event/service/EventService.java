package leets.weeth.domain.event.service;

import jakarta.persistence.EntityNotFoundException;
import leets.weeth.domain.event.dto.RequestEvent;
import leets.weeth.domain.event.dto.ResponseEvent;
import leets.weeth.domain.event.entity.Event;
import leets.weeth.domain.event.entity.enums.Status;
import leets.weeth.domain.event.mapper.EventMapper;
import leets.weeth.domain.event.repository.EventRepository;
import leets.weeth.domain.user.entity.User;
import leets.weeth.domain.user.repository.UserRepository;
import leets.weeth.global.common.error.exception.custom.BusinessLogicException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

import static leets.weeth.domain.event.entity.enums.ErrorMessage.*;


@Service
@RequiredArgsConstructor
public class EventService {
    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    private final EventMapper eventMapper;

    // 일정 생성
    @Transactional
    public void createEvent(RequestEvent requestEvent, Long userId) throws BusinessLogicException {
        // 기간 입력 검증
        LocalDateTime start = requestEvent.startDateTime();
        LocalDateTime end = requestEvent.endDateTime();
        validateDateRange(start, end);

        // 이벤트면 이벤트 어탠던스면 어탠던스로 저장
        Status status = requestEvent.status();

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(USER_NOT_FOUND.getMessage()));

        eventRepository.save(Event.fromDto(requestEvent, status, user));
    }


    // 일정 상세 조회
    @Transactional(readOnly = true)
    public ResponseEvent getEventById(Long id) {

        // 일정은 다 반환해도 됨, 캘린더에서 보여아하니
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

    // 년도 별 일정 조회
    public Map<Integer, List<ResponseEvent>> getEventsOfYear(int year) {
        // 1년치 일정을 모두 조회
        LocalDateTime start = LocalDateTime.of(year, 1, 1, 0, 0);
        LocalDateTime end = LocalDateTime.of(year, 12, 31, 23, 59);
        List<Event> events = eventRepository.findByStartDateTimeBetween(start, end);

        Map<Integer, List<ResponseEvent>> eventsByMonth = new HashMap<>();
        for (Event event : events) {
            LocalDateTime eventStart = event.getStartDateTime();
            LocalDateTime eventEnd = event.getEndDateTime();

            // 각 월을 반복하며 해당 월에 이벤트를 추가
            while (!eventStart.isAfter(eventEnd)) {
                int month = eventStart.getMonthValue();
                eventsByMonth
                        .computeIfAbsent(month, k -> new ArrayList<>())
                        .add(eventMapper.toDto(event));
                // 한달씩 증가
                eventStart = eventStart.plusMonths(1).withDayOfMonth(1)
                        .withHour(0).withMinute(0).withSecond(0).withNano(0);
            }
        }

        // 각 월의 이벤트를 시작 시간 순서대로 정렬
        eventsByMonth.values().forEach(eventList ->
                eventList.sort(Comparator.comparing(ResponseEvent::startDateTime))
        );

        return eventsByMonth;
    }

    // 일정 수정
    @Transactional
    public void updateEvent(Long eventId, RequestEvent updatedEvent, Long userId) throws BusinessLogicException {
        // 일정을 생성한 사용자인지 확인
        Event oldEvent = validateEventOwner(eventId, userId);

        oldEvent.updateFromDto(updatedEvent);
    }


    // 일정 삭제
    @Transactional
    public void deleteEvent(Long eventId, Long userId) throws BusinessLogicException {
        // 일정을 생성한 사용자인지 확인
        Event oldEvent = validateEventOwner(eventId, userId);
        if(oldEvent.getStatus().equals(Status.NOTICE)){
            //예외처리 공지는 공지사항에서 삭제해주세요
        }
        // 이벤트.delete로 온 요청이 공지를 삭제한다면 예외처리, 아니라면 삭제
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


}
