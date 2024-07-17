package leets.weeth.domain.attendance.service;

import leets.weeth.domain.attendance.dto.RequestAttendance;
import leets.weeth.domain.attendance.dto.ResponseAttendance;
import leets.weeth.domain.attendance.entity.Attendance;
import leets.weeth.domain.attendance.entity.AttendanceCode;
import leets.weeth.domain.attendance.entity.enums.WeekEnum;
import leets.weeth.domain.attendance.repository.AttendanceCodeRepository;
import leets.weeth.domain.attendance.repository.AttendanceRepository;
import leets.weeth.domain.event.entity.Event;
import leets.weeth.domain.event.repository.EventRepository;
import leets.weeth.domain.user.entity.User;
import leets.weeth.domain.user.repository.UserRepository;
import leets.weeth.global.common.error.exception.custom.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final AttendanceCodeRepository attendanceCodeRepository;

    public ResponseAttendance recordAttendance(RequestAttendance requestDto, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);

        LocalDateTime now = LocalDateTime.now();
        List<Event> currentEvents = eventRepository.findByStartDateTimeBetween(now.minusMinutes(1), now.plusMinutes(1));
        Event currentEvent = currentEvents.stream()
                .filter(event -> event.getTitle().equals(requestDto.getAttendanceCode()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("현재 시간에 해당하는 모임이 없습니다."));

        AttendanceCode attendanceCode = attendanceCodeRepository.findByWeekAndExpirationTimeAfter(calculateWeek(now), now)
                .orElseThrow(() -> new IllegalArgumentException("유효한 출석 코드가 없습니다."));

        if (!attendanceCode.getAttendanceCode().equals(requestDto.getAttendanceCode())) {
            throw new IllegalArgumentException("잘못된 출석 코드입니다.");
        }

        WeekEnum weekEnum = calculateWeek(now);
        Attendance attendance = attendanceRepository.findByUserAndWeek(user, weekEnum)
                .orElse(Attendance.builder()
                        .user(user)
                        .attendanceCode(requestDto.getAttendanceCode())
                        .isAttend(true)
                        .week(weekEnum)
                        .build());

        attendanceRepository.save(attendance);

        long totalAttendances = attendanceRepository.countByUserAndIsAttendTrue(user);
        long totalAbsences = attendanceRepository.countByUserAndIsAttendFalse(user);
        long totalRecords = totalAttendances + totalAbsences;

        double attendanceRate = totalRecords > 0 ? (double) totalAttendances / totalRecords * 100 : 0;
        double absenceRate = totalRecords > 0 ? (double) totalAbsences / totalRecords * 100 : 0;

        return ResponseAttendance.create(
                currentEvent.getTitle(),
                currentEvent.getStartDateTime().toString(),
                currentEvent.getLocation(),
                attendanceRate,
                absenceRate
        );
    }

    private WeekEnum calculateWeek(LocalDateTime now) {
        int weekNumber = (now.getDayOfYear() - 1) / 7;
        return WeekEnum.values()[weekNumber];
    }
}
