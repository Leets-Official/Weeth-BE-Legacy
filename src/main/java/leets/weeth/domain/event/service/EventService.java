package leets.weeth.domain.event.service;

import jakarta.persistence.EntityNotFoundException;
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

    private final EventMapper eventMapper;

    // 일정 생성
    @Transactional
    public void createEvent(RequestEvent requestEvent, String userEmail) throws BusinessLogicException {
        // 기간 입력 검증
        validateDateRange(requestEvent.startDateTime(), requestEvent.endDateTime());

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new EntityNotFoundException(USER_NOT_FOUND.getMessage()));
        // 유저 정보 저장을 위해 매퍼를 제거하고 정적 팩토리 메서드 사용
        eventRepository.save(Event.fromDto(requestEvent, user));
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

    // 일정 수정
    @Transactional
    public void updateEvent(Long eventId, RequestEvent updatedEvent, String userEmail) throws BusinessLogicException {
        // 일정을 생성한 사용자인지 확인
        Event oldEvent = validateEventOwner(eventId, userEmail);
        oldEvent.updateFromDto(updatedEvent);
    }

    // 일정 삭제
    @Transactional
    public void deleteEvent(Long eventId, String userEmail) throws BusinessLogicException {
        // 일정을 생성한 사용자인지 확인
        Event oldEvent = validateEventOwner(eventId, userEmail);
        eventRepository.delete(oldEvent);
    }

    // 해당 일정을 생성한 사용자와 같은지 검증
    private Event validateEventOwner(Long eventId, String userEmail) throws BusinessLogicException {
        Event oldEvent = eventRepository.findById(eventId)
                .orElseThrow(() -> new EntityNotFoundException(EVENT_NOT_FOUND.getMessage()));

        User user = userRepository.findByEmail(userEmail)
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
