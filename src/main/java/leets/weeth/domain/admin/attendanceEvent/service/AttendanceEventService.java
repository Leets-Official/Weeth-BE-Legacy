package leets.weeth.domain.admin.attendanceEvent.service;

import leets.weeth.domain.admin.attendanceEvent.dto.RequestAttendanceEvent;
import leets.weeth.domain.admin.attendanceEvent.mapper.AttendanceEventMapper;
import leets.weeth.domain.event.repository.EventRepository;
import leets.weeth.domain.user.entity.User;
import leets.weeth.domain.user.repository.UserRepository;
import leets.weeth.global.common.error.exception.custom.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AttendanceEventService {
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final AttendanceEventMapper attendanceEventMapper;

    // 출석 일정 생성
    @Transactional
    public void createAttendanceEvent(RequestAttendanceEvent requestAttendanceEvent, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);

        // type은 ATTENDANCE로, location은 null로 된 일정 생성
        eventRepository.save(attendanceEventMapper.fromAttendanceEventDto(requestAttendanceEvent, user));
    }
    /*
        * 수정, 조회, 삭제는 관리자가 직접 수행
        * 출석일정을 통해 출석정보를 생성하기 때문에 출석일정 수정시 문제가 일어날 가능성 증가
        * 관리자가 DB 단에서 직접 수정, 삭제할 예정
     */
}
