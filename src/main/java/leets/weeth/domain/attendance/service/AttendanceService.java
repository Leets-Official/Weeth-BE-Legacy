package leets.weeth.domain.attendance.service;

import jakarta.validation.ValidationException;
import leets.weeth.domain.attendance.dto.RequestAttendance;
import leets.weeth.domain.attendance.entity.Attendance;
import leets.weeth.domain.attendance.entity.AttendanceCode;
import leets.weeth.domain.attendance.entity.enums.Week;
import leets.weeth.domain.user.entity.User;
import leets.weeth.domain.user.repository.UserRepository;
import leets.weeth.domain.attendance.repository.AttendanceRepository;
import leets.weeth.domain.attendance.exception.AttendanceCodeException;
import leets.weeth.domain.attendance.exception.ResourceNotFoundException;
import leets.weeth.domain.attendance.repository.AttendanceCodeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.WeekFields;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AttendanceService {

    private final AttendanceCodeRepository attendanceCodeRepository;
    private final AttendanceRepository attendanceRepository;
    private final UserRepository userRepository;
    private final AttendanceStatisticsService attendanceStatisticsService;

    public void checkInAttendance(RequestAttendance requestAttendance, String currentEmail) {
        LocalDateTime now = LocalDateTime.now();
        int currentWeek = calculateCurrentWeek(now);

        AttendanceCode attendanceCode = attendanceCodeRepository.findByWeekAndDate(Week.of(currentWeek), now.toLocalDate())
                .orElseThrow(() -> new ResourceNotFoundException("해당 주차의 출석 코드를 찾을 수 없습니다."));

        if (Objects.equals(attendanceCode.getAttendanceCode(), requestAttendance.getAttendanceCode())) {
            if (attendanceCode.getExpirationTime().isBefore(now)) {  //코드 만료시
                throw new AttendanceCodeException("출석 코드가 유효기간이 지났습니다.");
            }

            User user = userRepository.findByEmail(currentEmail)
                    .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));

            Attendance attendance = new Attendance();
            attendance.setAttend(true);
            attendance.setUser(user);
            attendance.setWeek(Week.of(currentWeek));
            attendanceRepository.save(attendance);

            attendanceStatisticsService.updateAttendanceRate(user.getId());
        } else {
            throw new AttendanceCodeException("출석 코드가 유효하지 않습니다.");
        }
    }
    private int calculateCurrentWeek(LocalDateTime now) {
        return now.get(WeekFields.ISO.weekOfYear());
    }
}
