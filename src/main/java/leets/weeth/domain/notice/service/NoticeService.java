package leets.weeth.domain.notice.service;

import leets.weeth.domain.event.entity.Event;
import leets.weeth.domain.event.entity.enums.Type;
import leets.weeth.domain.event.repository.EventRepository;
import leets.weeth.domain.notice.dto.RequestNotice;
import leets.weeth.domain.notice.dto.ResponseNotice;
import leets.weeth.domain.notice.mapper.NoticeMapper;
import leets.weeth.domain.user.entity.User;
import leets.weeth.domain.user.repository.UserRepository;
import leets.weeth.global.common.error.exception.custom.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

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

        // 일정에 저장시 startDateTime과 endDateTime을 현재 시간으로 저장
        LocalDateTime now = LocalDateTime.now();
        // 상태는 NOTICE로 저장
        eventRepository.save(noticeMapper.fromNoticeDto(requestNotice, user));
    }

    // 모든 공지사항 조회
    @Transactional(readOnly = true)
    public List<ResponseNotice> getNotices(){
        // 상태가 NOTICE인 모든 공지사항을 ID에 오름차순으로 조회
        List<Event> events = eventRepository.findAllByType(Type.NOTICE, Sort.by(Sort.Direction.ASC, "id"));

        return events.stream()
                .map(noticeMapper::toNoticeDto)
                .toList();
    }

    // 공지 상세 조회
    @Transactional(readOnly = true)
    public ResponseNotice getNoticeById(Long id) throws TypeNotMatchException {
        // getNotice로 온 요청엔 공지사항 게시판에서 온 요청이라 가정. NOTICE만 반환
        Event event = eventRepository.findByIdAndType(id, Type.NOTICE)
                .orElseThrow(TypeNotMatchException::new);//예외 수정 statusNotMatch

        return noticeMapper.toNoticeDto(event);
    }

    // 공지 수정
    @Transactional
    public void updateNotice(Long noticeId, RequestNotice requestNotice, Long userId) throws BusinessLogicException {
        // 공지사항을 생성한 사용자인지 확인
        Event oldEvent = validateNoticeOwner(noticeId, userId);

        // 해당 일정의 상태가 NOTICE 인지 확인
        if(oldEvent.getType().equals(Type.NOTICE)) {
            LocalDateTime now = LocalDateTime.now();
            oldEvent.updateFromNoticeDto(requestNotice, now);
        } else {
            throw new TypeNotMatchException();
        }
    }


    // 공지 삭제
    @Transactional
    public void deleteNotice(Long noticeId, Long userId) throws BusinessLogicException {
        // 공지사항을 생성한 사용자인지 확인
        Event oldEvent = validateNoticeOwner(noticeId, userId);

        // 해당 일정의 상태가 NOTICE 인지 확인
        if(oldEvent.getType().equals(Type.NOTICE)) {
            eventRepository.deleteById(noticeId);
        } else {
            throw new TypeNotMatchException();
        }
    }

    // 해당 일정을 생성한 사용자와 같은지 검증
    private Event validateNoticeOwner(Long eventId, Long userId) throws UserNotMatchException {
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