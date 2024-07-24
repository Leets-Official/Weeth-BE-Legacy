package leets.weeth.domain.attendance.service;

import leets.weeth.domain.attendance.dto.RequestAttendance;
import leets.weeth.domain.attendance.dto.ResponseAttendance;
import leets.weeth.domain.attendance.dto.ResponseAttendanceDetails;
import leets.weeth.domain.attendance.entity.Attendance;
import leets.weeth.domain.attendance.entity.Week;
import leets.weeth.domain.attendance.repository.AttendanceRepository;
import leets.weeth.domain.attendance.repository.WeekRepository;
import leets.weeth.domain.event.entity.Event;
import leets.weeth.domain.event.entity.enums.Type;
import leets.weeth.domain.event.repository.EventRepository;
import leets.weeth.domain.user.entity.User;
import leets.weeth.domain.user.repository.UserRepository;
import leets.weeth.global.common.error.exception.custom.AttendanceCodeMismatchException;
import leets.weeth.global.common.error.exception.custom.UserNotFoundException;
import leets.weeth.global.common.error.exception.custom.AttendanceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final UserRepository userRepository;
    private final WeekRepository weekRepository;
    private final EventRepository eventRepository;

    //사용자가 출석 코드를 입력하여 출석 검증 메서드
    @SneakyThrows
    public ResponseAttendance recordAttendance(RequestAttendance requestDto, Long userId) {
        //유저 정보 조회
        User user = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);
        LocalDateTime now = LocalDateTime.now();
        //출석 코드에 해당하는 주차 정보 조회
        Week week = weekRepository.findByAttendanceCode(requestDto.getAttendanceCode())
                .orElseThrow(AttendanceCodeMismatchException::new);
        //사용자와 주차 정보에 해당하는 출석 정보 조회
        Attendance attendance = attendanceRepository.findAllByUserAndWeek(user, week)
                .orElseThrow(AttendanceNotFoundException::new);
        //출석 시간이 정기 모임 시간 내에 있는지
//        if (!isWithinAttendanceTime(now)) {
//            throw new EventNotFoundException();
//        }
        //출석 여부 true로 변경 후 저장
        attendance.setIsAttend(true);
        attendanceRepository.save(attendance);
        //출석 검증 로직 응답dto
        return ResponseAttendance.builder()
                .attendanceRate(calculateAttendanceRate(user))
                .attendanceDate(now)
                .build();
    }

    // 정기 모임 시간 내에 있는지 확인하는 메서드
//    private boolean isWithinAttendanceTime(LocalDateTime now) {
//        List<Event> events = eventRepository.findAllByType(Type.ATTENDANCE);
//        for (Event event : events) {
//            if (now.isAfter(event.getStartDateTime()) && now.isBefore(event.getEndDateTime())) {
//                return true;
//            }
//        }
//        return false;
//    }

    //사용자 출석률 계산
    private int calculateAttendanceRate(User user) {
        long totalAttendances = attendanceRepository.countByUserAndIsAttendTrue(user);
        long totalEvents = eventRepository.countTotalByType(Type.ATTENDANCE);

        if (totalEvents == 0) return 0; //이벤트가 없을 시 출석률 조회 0으로
        return (int) ((totalAttendances * 100) / totalEvents);
    }

    // 사용자의 모든 출석 정보를 조회하는 메서드
    public List<ResponseAttendanceDetails> getAllAttendanceDetails(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);

        // 총 정기모임 횟수
        long totalMeetingsCount = eventRepository.countTotalByType(Type.ATTENDANCE);

        // 총 출석 횟수
        long totalAttendanceCount = attendanceRepository.countByUserAndIsAttendTrue(user);

        // 총 결석 횟수
        long totalAbsenceCount = totalMeetingsCount - totalAttendanceCount;

        List<Attendance> attendances = attendanceRepository.findAllByUser(user);
        return attendances.stream().map(attendance ->
                ResponseAttendanceDetails.builder()
                        .attendanceId(attendance.getAttendanceId())
                        .totalAttendanceCount(totalAttendanceCount)
                        .totalAbsenceCount(totalAbsenceCount)
                        .totalMeetingsCount(totalMeetingsCount)
                        .build()
        ).collect(Collectors.toList());
    }
    //회원가입 승인과 연계할 메서드
    public void createAttendancesForUser(User user) {
        List<Week> weeks = weekRepository.findAll();
        for (Week week : weeks) {
            Attendance attendance = Attendance.builder()
                    .user(user)
                    .week(week)
                    .isAttend(false)
                    .build();
            attendanceRepository.save(attendance);
        }
    }
}
