package leets.weeth.domain.event.service;

import leets.weeth.domain.event.dto.RequestEvent;
import leets.weeth.domain.event.dto.ResponseEvent;
import leets.weeth.domain.event.entity.Event;
import leets.weeth.domain.event.entity.enums.Type;
import leets.weeth.domain.event.mapper.EventMapper;
import leets.weeth.domain.event.repository.EventRepository;
import leets.weeth.domain.user.entity.User;
import leets.weeth.domain.user.repository.UserRepository;
import leets.weeth.global.common.error.exception.custom.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;


@Service
@RequiredArgsConstructor
public class EventService {
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final EventMapper eventMapper;

    // 일정 생성
    @Transactional
    public void createEvent(RequestEvent requestEvent, Long userId) throws InvalidInputDateException {
        // 기간 입력 검증
        validateDateRange(requestEvent.startDateTime(), requestEvent.endDateTime());

        User user = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);

        eventRepository.save(eventMapper.fromEventDto(requestEvent, user));
    }

    // 일정 상세 조회
    @Transactional(readOnly = true)
    public ResponseEvent getEventById(Long eventId) {
        // 일정은 다 반환해도 됨, 캘린더에서 보여야하니
        Event event = eventRepository.findById(eventId)
                .orElseThrow(EventNotFoundException::new);

        return eventMapper.toEventDto(event);
    }

    // 기간 별 일정 조회
    @Transactional(readOnly = true)
    public List<ResponseEvent> getEventsBetweenDate(LocalDateTime startDate, LocalDateTime endDate) throws InvalidInputDateException {
        // 기간 입력 검증
        validateDateRange(startDate, endDate);

        List<Event> events = eventRepository.findByStartDateTimeBetween(startDate, endDate);
        return events.stream()
                .map(eventMapper::toEventDto)
                .toList();
    }

    // 년도 별 일정 조회
    @Transactional(readOnly = true)
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
                        .add(eventMapper.toEventDto(event));
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
        Event oldEvent = eventRepository.findById(eventId)
                .orElseThrow(EventNotFoundException::new);

        validateEventOwner(oldEvent, userId);
        oldEvent.updateFromEventDto(updatedEvent);
    }

    // 일정 삭제
    @Transactional
    public void deleteEvent(Long eventId, Long userId) throws BusinessLogicException {
        // 일정을 생성한 사용자인지 확인
        Event event = eventRepository.findById(eventId)
                .orElseThrow(EventNotFoundException::new);

        validateEventOwner(event, userId);
        eventRepository.deleteById(eventId);
    }

    // 검색된 event가 EVENT 인지 확인, 맞으면 생성한 사용자와 현재 사용자가 동일한지 확인
    private void validateEventOwner(Event event, Long userId) throws BusinessLogicException {
        // 해당 일정이 EVENT 인지 확인 -> 출석은 이후 따로 구현할 예정
        // 출석은 여기서 수정해도 되지 않나..? -> 팀원들 얘기 들어보기
        if(!event.getType().equals(Type.EVENT)){
            throw new TypeNotMatchException();
        }
        // 일정을 생성한 사용자와 같은지 확인
        // userId는 JWT 토큰에서 추출하므로 굳이 user 객체를 DB에서 조회하지 않고 비교해도 될 것 같다는 판단
        if(!event.getUser().getId().equals(userId)){
            throw new UserNotMatchException();
        }
    }

    // 시작 날짜가 종료 날짜 보다 느린지 검증
    private void validateDateRange(LocalDateTime start, LocalDateTime end) throws InvalidInputDateException {
        if (start.isAfter(end)) {
            throw new InvalidInputDateException();
        }
    }
}
