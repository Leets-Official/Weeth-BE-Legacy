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
    @Transactional
    public ResponseEvent getEventById(Long id) {
        Event event = eventRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        return eventMapper.toDto(event);
    }
}
