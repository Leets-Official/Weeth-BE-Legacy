package leets.weeth.domain.attendance.service;

import leets.weeth.domain.attendance.dto.RequestAttendance;
import leets.weeth.domain.attendance.dto.ResponseAttendance;
import leets.weeth.domain.attendance.entity.Attendance;
import leets.weeth.domain.attendance.repository.AttendanceRepository;
import leets.weeth.domain.attendance.repository.WeekRepository;
import leets.weeth.domain.attendance.entity.Week;
import leets.weeth.domain.event.entity.Event;
import leets.weeth.domain.event.entity.enums.Type;
import leets.weeth.domain.event.repository.EventRepository;
import leets.weeth.domain.user.entity.User;
import leets.weeth.domain.user.repository.UserRepository;
import leets.weeth.global.common.error.exception.custom.AttendanceCodeMismatchException;
import leets.weeth.global.common.error.exception.custom.UserNotFoundException;
import leets.weeth.global.common.error.exception.custom.EventNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final UserRepository userRepository;
    private final WeekRepository weekRepository;
    private final EventRepository eventRepository;

    @SneakyThrows
    public ResponseAttendance recordAttendance(RequestAttendance requestDto, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);

        Week week = weekRepository.findByAttendanceCode(requestDto.getAttendanceCode())
                .orElseThrow(AttendanceCodeMismatchException::new);

        LocalDateTime now = LocalDateTime.now();

//        // 현재 시각에 해당하는 ATTENDANCE 타입의 이벤트 조회
//        Event attendanceEvent = eventRepository.findByTypeAndStartDateTimeBeforeAndEndDateTimeAfter(
//                        Type.ATTENDANCE, now, now)
//                .orElseThrow(EventNotFoundException::new);

        Attendance attendance = attendanceRepository.findAllByUserAndWeek(user, week)
                .orElse(Attendance.builder()
                        .user(user)
                        .week(week)
                        .isAttend(false) // 기본값 false
                        .build());

        // 출석 상태를 true로 설정
        attendance.setIsAttend(true);

        // 엔티티를 저장
        attendanceRepository.save(attendance);

        // ResponseAttendance 생성
        return ResponseAttendance.builder()
//                .scheduleTitle(attendanceEvent.getTitle())
//                .scheduleDateTime(now.toString())
//                .scheduleLocation(attendanceEvent.getLocation())
                .attendanceRate(calculateAttendanceRate(user))
                .attendanceDate(now)
                .build();
    }

    private int calculateAttendanceRate(User user) {
        long totalAttendances = attendanceRepository.countByUserAndIsAttendTrue(user);
        long totalEvents = eventRepository.countTotalByType(Type.ATTENDANCE);

        if (totalEvents == 0) return 0; //이벤트가 없을시 출석률 조회 0으로
        return (int) ((totalAttendances * 100) / totalEvents);
    }
}
