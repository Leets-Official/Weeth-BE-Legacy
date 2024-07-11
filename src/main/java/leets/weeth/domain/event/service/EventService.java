package leets.weeth.domain.event.service;

import jakarta.persistence.EntityNotFoundException;
import leets.weeth.domain.event.dto.RequestEvent;
import leets.weeth.domain.event.dto.ResponseEvent;
import leets.weeth.domain.event.entity.Event;
import leets.weeth.domain.event.mapper.EventMapper;
import leets.weeth.domain.event.repository.EventRepository;
import leets.weeth.domain.user.entity.User;
import leets.weeth.domain.user.repository.UserRepository;
import leets.weeth.global.common.exception.BusinessLogicException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static leets.weeth.domain.event.entity.enums.ErrorMessage.EVENT_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class EventService {
    private final EventRepository eventRepository;

    private final UserRepository userRepository;

    private final EventMapper eventMapper;

    // 일정 생성
    @Transactional
    public void createEvent(RequestEvent requestEvent, String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 사용자입니다."));
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
    public List<ResponseEvent> getEventsBetweenDate(LocalDateTime startDate, LocalDateTime endDate) {
        List<Event> events = eventRepository.findByStartDateTimeBetween(startDate, endDate);
        return events.stream()
                .map(eventMapper::toDto)
                .toList();
    }

    // 일정 수정
    @Transactional
    public void updateEvent(Long id, RequestEvent updatedEvent, String userEmail) throws BusinessLogicException {
        Event oldEvent = eventRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(EVENT_NOT_FOUND.getMessage()));
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 사용자입니다."));
        if(!user.getId().equals(oldEvent.getUser().getId())){
            throw new BusinessLogicException("일정을 작성한 사용자만 수정할 수 있습니다.");
        }
        oldEvent.updateFromDto(updatedEvent);
    }

    // 일정 삭제
    public void deleteEvent(Long eventId, String userEmail) throws BusinessLogicException {

        Event oldEvent = eventRepository.findById(eventId)
                .orElseThrow(() -> new EntityNotFoundException(EVENT_NOT_FOUND.getMessage()));
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 사용자입니다."));
        if(!user.getId().equals(oldEvent.getUser().getId())){
            throw new BusinessLogicException("일정을 작성한 사용자만 삭제할 수 있습니다.");
        }
        eventRepository.deleteByIdAndUserId(eventId, user.getId());

    }
}
