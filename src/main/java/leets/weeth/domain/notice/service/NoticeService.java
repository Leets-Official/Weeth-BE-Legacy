package leets.weeth.domain.notice.service;

import jakarta.persistence.EntityNotFoundException;
import leets.weeth.domain.event.dto.RequestEvent;
import leets.weeth.domain.event.entity.Event;
import leets.weeth.domain.event.entity.enums.Status;
import leets.weeth.domain.notice.dto.RequestNotice;
import leets.weeth.domain.notice.dto.ResponseNotice;
import leets.weeth.domain.event.repository.EventRepository;
import leets.weeth.domain.notice.mapper.NoticeMapper;
import leets.weeth.domain.user.entity.User;
import leets.weeth.domain.user.repository.UserRepository;
import leets.weeth.global.common.error.exception.custom.BusinessLogicException;
import leets.weeth.global.common.error.exception.custom.NoticeNotFoundException;
import leets.weeth.global.common.error.exception.custom.UserNotFoundException;
import leets.weeth.global.common.error.exception.custom.UserNotMatchException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static leets.weeth.domain.event.entity.enums.ErrorMessage.*;

@Service
@RequiredArgsConstructor
public class NoticeService {
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final NoticeMapper noticeMapper;

    // 공지생성
    @Transactional
    public void createNotice(RequestNotice requestNotice, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);

        LocalDateTime now = LocalDateTime.now();
        eventRepository.save(Event.fromNoticeDto(requestNotice, Status.NOTICE, user, now));
    }

    // 모든 공지사항 조회
    @Transactional(readOnly = true)
    public List<ResponseNotice> getNotices(){
        List<Event> events = eventRepository.findAllByStatus(Status.NOTICE, Sort.by(Sort.Direction.ASC, "id"));

        return events.stream()
                .map(noticeMapper::toDto)
                .toList();
    }

    // 공지 상세 조회
    @Transactional(readOnly = true)
    public ResponseNotice getNoticeById(Long id) {
        // getNotice로 온 요청엔 공지사항 게시판에서 온 요청이라 가정. NOTICE만 반환
        Event event = eventRepository.findByIdAndStatus(id, Status.NOTICE)
                .orElseThrow(() -> new EntityNotFoundException(EVENT_NOT_FOUND.getMessage()));//예외 수정 statusNotMatch


        return noticeMapper.toDto(event);
    }

    // 공지 수정
    @Transactional
    public void updateNotice(Long eventId, RequestNotice requestNotice, Long userId) throws BusinessLogicException {
        // 일정을 생성한 사용자인지 확인
        Event oldEvent = validateEventOwner(eventId, userId);

        if(oldEvent.getStatus().equals(Status.NOTICE)) {
            LocalDateTime now = LocalDateTime.now();

            oldEvent.updateFromNoticeDto(requestNotice, now);
        } else {
            System.out.println("공지사항이 아닙니다");
        }
    }


    // 공지 삭제
    @Transactional
    public void deleteNotice(Long noticeId, Long userId) throws UserNotMatchException {
        // 공지사항이 맞는지 확인
        Event oldEvent = validateEventOwner(noticeId, userId);

        if(oldEvent.getStatus().equals(Status.NOTICE)) {
            eventRepository.deleteById(noticeId);
        } else {//예외처리: 공지사항이 아니라 삭제가 안된다
        }
    }

    // 해당 일정을 생성한 사용자와 같은지 검증
    private Event validateEventOwner(Long eventId, Long userId) throws UserNotMatchException {
        Event oldEvent = eventRepository.findById(eventId)
                .orElseThrow(NoticeNotFoundException::new);

        User user = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);
        // -> findByIdAndUserId로 변경 / 중복되긴 하는데 이게 의미상 더 맞는 듯

        // 일정을 생성한 사용자와 같은지 확인
        if(!user.getId().equals(oldEvent.getUser().getId())){
            throw new UserNotMatchException();
        }
        return oldEvent;
    }

}