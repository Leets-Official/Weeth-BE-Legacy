package leets.weeth.domain.user.service;

import leets.weeth.domain.user.dto.RequestEvent;
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

    @Transactional
    public void createEvent(RequestEvent requestEvent) {
        Event event = eventMapper.fromDto(requestEvent);
        eventRepository.save(event);
    }


}
