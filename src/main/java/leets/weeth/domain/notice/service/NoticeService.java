package leets.weeth.domain.notice.service;

import leets.weeth.domain.event.entity.Event;
import leets.weeth.domain.event.entity.enums.Type;
import leets.weeth.domain.event.repository.EventRepository;
import leets.weeth.domain.file.service.FileService;
import leets.weeth.domain.notice.dto.RequestNotice;
import leets.weeth.domain.notice.dto.ResponseNotice;
import leets.weeth.domain.notice.mapper.NoticeMapper;
import leets.weeth.domain.user.entity.User;
import leets.weeth.domain.user.repository.UserRepository;
import leets.weeth.global.common.error.exception.custom.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NoticeService {
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final FileService fileService;
    private final NoticeMapper noticeMapper;

    /*
     * NoticeController로 온 생성 요청 -> TYPE.NOTICE로 생성
     * 조회, 수정, 삭제 시  TYPE.NOTICE만 가능
     */

    // 공지 생성
    @Transactional
    public void createNotice(RequestNotice requestNotice, List<MultipartFile> files, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);

        List<String> fileUrls = fileService.uploadFiles(files);
        eventRepository.save(noticeMapper.fromNoticeDto(requestNotice, fileUrls, user));
    }

    // 공지 상세 조회
    @Transactional(readOnly = true)
    public ResponseNotice getNoticeById(Long noticeId) throws TypeNotMatchException {
        // 해당 일정이 존재하는지 먼저 확인
        Event event = eventRepository.findById(noticeId)
                .orElseThrow(NoticeNotFoundException::new);

        // 존재한다면 type이 NOTICE 인지 확인
        if (!event.getType().equals(Type.NOTICE)) {
            throw new TypeNotMatchException();
        }
        return noticeMapper.toNoticeDto(event);
    }

    // 모든 공지사항 조회
    @Transactional(readOnly = true)
    public List<ResponseNotice> getAllNotices() {
        // 상태가 NOTICE인 모든 공지사항을 ID에 오름차순으로 조회
        List<Event> events = eventRepository.findAllByType(Type.NOTICE, Sort.by(Sort.Direction.ASC, "id"));

        return events.stream()
                .map(noticeMapper::toNoticeDto)
                .toList();
    }

    // 공지 수정
    @Transactional
    public void updateNotice(Long noticeId, RequestNotice requestNotice, List<MultipartFile> files, Long userId) throws BusinessLogicException {
        Event oldNotice = eventRepository.findById(noticeId)
                .orElseThrow(NoticeNotFoundException::new);

        validateNoticeOwner(oldNotice, userId);
        List<String> urls = fileService.uploadFiles(files);

        oldNotice.updateFromNoticeDto(requestNotice, urls);
    }

    // 공지 삭제
    @Transactional
    public void deleteNotice(Long noticeId, Long userId) throws BusinessLogicException {
        Event notice = eventRepository.findById(noticeId)
                .orElseThrow(NoticeNotFoundException::new);

        validateNoticeOwner(notice, userId);
        eventRepository.deleteById(noticeId);
    }

    // 검색된 event가 notice인지 확인, 맞으면 생성한 사용자와 현재 사용자가 동일한지 확인
    private void validateNoticeOwner(Event notice, Long userId) throws BusinessLogicException {
        // 해당 일정이 NOTICE 인지 확인
        if (!notice.getType().equals(Type.NOTICE)) {
            throw new TypeNotMatchException();
        }
        // 공지사항을 생성한 사용자와 같은지 확인
        if (!notice.getUser().getId().equals(userId)) {
            throw new UserNotMatchException();
        }
    }

    public List<ResponseNotice> loadNotices(Long lastNoticeId) throws InvalidAccessException {
        Long maxNoticeId = eventRepository.findMaxNoticeId(Type.NOTICE);

        if(lastNoticeId==null){   // 첫번째 요청인 경우
            lastNoticeId = maxNoticeId + 1;
        }
        if(lastNoticeId <= 1 || lastNoticeId > maxNoticeId + 1){
            throw new InvalidAccessException(); // lastNoticeId가 1 이하이거나 최대값보다 클경우
        }

        Pageable pageable = PageRequest.of(0, 15); // 첫 페이지, 페이지당 15개 게시글
        List<Event> recentNotices = eventRepository.findRecentNoticesBytype(lastNoticeId, Type.NOTICE, pageable);
        return recentNotices.stream()
                .map(noticeMapper::toNoticeDto)
                .toList();
    }
}