package leets.weeth.domain.attendance.service;

import leets.weeth.domain.attendance.dto.RequestAttendance;
import leets.weeth.domain.attendance.dto.ResponseAttendance;
import leets.weeth.domain.attendance.entity.Attendance;
import leets.weeth.domain.attendance.entity.enums.Week;
import leets.weeth.domain.attendance.repository.AttendanceRepository;
import leets.weeth.domain.event.entity.Event;
import leets.weeth.domain.event.repository.EventRepository;
import leets.weeth.domain.user.entity.User;
import leets.weeth.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;

    public ResponseAttendance recordAttendance(RequestAttendance requestDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));

        // 현재 시간으로 이벤트 조회
        LocalDateTime now = LocalDateTime.now();
        List<Event> currentEvents = eventRepository.findByStartDateTimeBetween(now.minusMinutes(1), now.plusMinutes(1));
        Event currentEvent = currentEvents.stream()
                .filter(event -> event.getTitle().equals(requestDto.getAttendanceCode()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("현재 시간에 해당하는 이벤트가 없습니다."));

        if (!currentEvent.getTitle().equals(requestDto.getAttendanceCode())) {
            throw new IllegalArgumentException("잘못된 출석 코드입니다.");
        }

        //주차 계산 (일단 단순한 예시로 주차를 계산함)
        Week week = calculateWeek(now);

        //출석 기록 조회 및 업데이트
        Attendance attendance = attendanceRepository.findByUserAndWeek(user, week)
                .orElse(new Attendance());
        attendance.setUser(user);
        attendance.setAttendanceCode(requestDto.getAttendanceCode());
        attendance.setAttend(true);
        attendance.setWeek(week);
        attendanceRepository.save(attendance);

        //응답 DTO 생성
        ResponseAttendance responseDto = new ResponseAttendance();
        responseDto.setScheduleTitle(currentEvent.getTitle());
        responseDto.setScheduleDateTime(currentEvent.getStartDateTime().toString());
        responseDto.setScheduleLocation(currentEvent.getLocation());

        //출석률 및 결석률 계산
        long totalAttendances = attendanceRepository.countByUserAndIsAttendTrue(user);
        responseDto.setAttendanceRate(totalAttendances * 100);
        responseDto.setAbsenceRate((1 - totalAttendances) * 100);

        return responseDto;
    }

    private Week calculateWeek(LocalDateTime now) {
        //주차 계산 로직
        int weekNumber = now.getDayOfYear() / 7;
        return Week.of(weekNumber);
    }
}
