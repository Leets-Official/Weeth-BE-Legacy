package leets.weeth.domain.notice.service;

import jakarta.persistence.EntityNotFoundException;
import leets.weeth.domain.event.dto.RequestEvent;
import leets.weeth.domain.event.entity.Event;
import leets.weeth.domain.event.entity.enums.Status;
import leets.weeth.domain.notice.dto.RequestNotice;
import leets.weeth.domain.notice.dto.ResponseNotice;
import leets.weeth.domain.event.repository.EventRepository;
import leets.weeth.domain.user.entity.User;
import leets.weeth.domain.user.repository.UserRepository;
import leets.weeth.global.common.error.exception.custom.BusinessLogicException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static leets.weeth.domain.event.entity.enums.ErrorMessage.*;

@Service
@RequiredArgsConstructor
public class NoticeService {
    private final EventRepository eventRepository;
    private final UserRepository userRepository;


    // 공지생성
    @Transactional
    public void createNotice(RequestNotice requestNotice, Long userId) throws BusinessLogicException {

        LocalDateTime start = requestNotice.time();
        LocalDateTime end = requestNotice.time();
        RequestEvent requestEvent = RequestEvent.builder()
                .title(requestNotice.title())
                .content(requestNotice.content())
                .startDateTime(start)
                .endDateTime(end)
                .location(null)// 장소는 null로 설정
                .build();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(USER_NOT_FOUND.getMessage()));

        eventRepository.save(Event.fromDto(requestEvent, Status.NOTICE, user));
    }


    // 공지 상세 조회
    @Transactional(readOnly = true)
    public ResponseNotice getNoticeById(Long id) {

        // getNotice로 온 요청엔 공지사항 게시판에서 온 요청이라 가정. NOTICE만 반환
        Event event = eventRepository.findByIdAndStatus(id, Status.NOTICE)
                .orElseThrow(() -> new EntityNotFoundException(EVENT_NOT_FOUND.getMessage()));

        return ResponseNotice.builder()
                .title(event.getTitle())
                .content(event.getContent())
                .created_at(event.getCreatedAt())
                .modified_at(event.getModifiedAt())
                .build();
    }



    // 공지 수정
    @Transactional
    public void updateNotice(Long eventId, RequestNotice requestNotice, Long userId) throws BusinessLogicException {
        // 일정을 생성한 사용자인지 확인
        Event oldEvent = validateEventOwner(eventId, userId);

        if(oldEvent.getStatus().equals(Status.NOTICE)) {
            LocalDateTime start = requestNotice.time();
            LocalDateTime end = requestNotice.time();
            RequestEvent updatedEvent = RequestEvent.builder()
                    .title(requestNotice.title())
                    .content(requestNotice.content())
                    .startDateTime(start)
                    .endDateTime(end)
                    .location(null)// 장소는 null로 설정
                    .build();
            oldEvent.updateFromDto(updatedEvent);
        } else {//예외처리: 공지사항이 아니라 수정이 안된다
        }
    }


    // 공지 삭제
    @Transactional
    public void deleteNotice(Long noticeId, Long userId) throws BusinessLogicException {
        // 공지사항이 맞는지 확인
        Event oldEvent = validateEventOwner(noticeId, userId);

        if(oldEvent.getStatus().equals(Status.NOTICE)) {
            eventRepository.deleteById(noticeId);
        } else {//예외처리: 공지사항이 아니라 삭제가 안된다
        }
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

}