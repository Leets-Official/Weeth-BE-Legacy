package leets.weeth.domain.attendance.service;

import leets.weeth.domain.attendance.dto.RequestAttendance;
import leets.weeth.domain.attendance.dto.ResponseAttendance;
import leets.weeth.domain.attendance.dto.ResponseAttendanceSummary;
import leets.weeth.domain.attendance.entity.Attendance;
import leets.weeth.domain.attendance.entity.enums.WeekEnum;
import leets.weeth.domain.attendance.repository.AttendanceRepository;
import leets.weeth.domain.event.entity.Event;
import leets.weeth.domain.event.repository.EventRepository;
import leets.weeth.domain.user.entity.User;
import leets.weeth.domain.user.repository.UserRepository;
import leets.weeth.domain.attendance.repository.AttendanceCodeRepository;
import leets.weeth.global.common.error.exception.custom.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

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

        Attendance attendance = attendanceRepository.findByUserAndWeek(user, calculateWeek(now))
                .orElse(Attendance.builder()
                        .user(user)
                        .attendanceCode(requestDto.getAttendanceCode())
                        .isAttend(true)
                        .week(calculateWeek(now))
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
                absenceRate,
                calculateWeek(now).getStartDate().toString(),
                calculateWeek(now).getEndDate().toString()
        );
    }

    @Transactional(readOnly = true)
    public ResponseAttendanceSummary getAttendanceSummary(User user) {
        List<Attendance> attendances = attendanceRepository.findByUser(user);
        List<Event> events = eventRepository.findAll();

        long totalEvents = events.size();
        long totalAttendances = attendanceRepository.countByUserAndIsAttendTrue(user);
        long totalAbsences = attendanceRepository.countByUserAndIsAttendFalse(user);

        List<ResponseAttendance> attendanceDetails = attendances.stream()
                .map(attendance -> {
                    Event event = events.stream()
                            .filter(e -> e.getTitle().equals(attendance.getAttendanceCode()))
                            .findFirst()
                            .orElseThrow(() -> new IllegalArgumentException("해당 출석 코드에 대한 이벤트가 없습니다."));
                    WeekEnum week = attendance.getWeek();
                    String weekStartDate = week.getStartDate().toString();
                    String weekEndDate = week.getEndDate().toString();

                    return ResponseAttendance.create(
                            event.getTitle(),
                            event.getStartDateTime().toString(),
                            event.getLocation(),
                            (double) totalAttendances / totalEvents * 100,
                            (double) totalAbsences / totalEvents * 100,
                            weekStartDate,
                            weekEndDate
                    );
                })
                .collect(Collectors.toList());

        return ResponseAttendanceSummary.builder()
                .totalEvents(totalEvents)
                .totalAttendances(totalAttendances)
                .totalAbsences(totalAbsences)
                .attendanceDetails(attendanceDetails)
                .build();
    }

    private WeekEnum calculateWeek(LocalDateTime now) {
        int weekNumber = (now.getDayOfYear() - 1) / 7;
        return WeekEnum.values()[weekNumber];
    }
}
