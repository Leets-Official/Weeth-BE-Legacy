package leets.weeth.domain.user.service;

import jakarta.persistence.EntityNotFoundException;
import leets.weeth.domain.user.dto.RequestEvent;
import leets.weeth.domain.user.dto.ResponseEvent;
import leets.weeth.domain.user.entity.Event;
import leets.weeth.domain.user.mapper.EventMapper;
import leets.weeth.domain.user.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventService {
    private final EventRepository eventRepository;

    private final EventMapper eventMapper;

    // 일정 생성
    @Transactional
    public void createEvent(RequestEvent requestEvent) {
        Event event = eventMapper.fromDto(requestEvent);
        eventRepository.save(event);
    }

    // 일정 상세 조회
    @Transactional(readOnly = true)
    public ResponseEvent getEventById(Long id) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("id에 해당하는 일정이 존재하지 않습니다."));
        return eventMapper.toDto(event);
    }

    // 기간 별 일정 조회
    @Transactional(readOnly = true)
    public List<ResponseEvent> getEventsBetweenDate(LocalDateTime startDate, LocalDateTime endDate) {
        List<Event> events = eventRepository.findByStartDateTimeBetween(startDate, endDate);
        return events.stream()
                .map(eventMapper::toDto)
                .collect(Collectors.toList());
    }

    // 일정 수정
    @Transactional
    public void updateEvent(Long id, RequestEvent requestEvent) {
        Event oldEvent = eventRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("id에 해당하는 일정이 존재하지 않습니다."));

        oldEvent.update(
                requestEvent.getTitle(),
                requestEvent.getContent(),
                requestEvent.getLocation(),
                requestEvent.getStartDateTime(),
                requestEvent.getEndDateTime()
        );
    }

    // 일정 삭제
    public void deleteEvent(Long id) {
        if (!eventRepository.existsById(id)) {
            throw new EntityNotFoundException("id에 해당하는 일정이 존재하지 않습니다.");
        }
        eventRepository.deleteById(id);
    }
}
