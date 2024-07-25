package leets.weeth.domain.attendance.service;

import leets.weeth.domain.attendance.dto.AttendanceDTO;
import leets.weeth.domain.attendance.dto.RequestAttendance;
import leets.weeth.domain.attendance.entity.Attendance;
import leets.weeth.domain.attendance.entity.Week;
import leets.weeth.domain.attendance.mapper.AttendanceMapper;
import leets.weeth.domain.attendance.repository.AttendanceRepository;
import leets.weeth.domain.attendance.repository.WeekRepository;
import leets.weeth.domain.event.entity.Event;
import leets.weeth.domain.event.repository.EventRepository;
import leets.weeth.domain.user.entity.User;
import leets.weeth.domain.user.repository.UserRepository;
import leets.weeth.global.common.error.exception.custom.*;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.IntStream;

import static leets.weeth.domain.event.entity.enums.Type.ATTENDANCE;

@Service
@RequiredArgsConstructor
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final UserRepository userRepository;
    private final WeekRepository weekRepository;
    private final EventRepository eventRepository;
    private final AttendanceMapper attendanceMapper;

    //사용자가 출석 코드를 입력하여 출석 검증 메서드
    @SneakyThrows
    public void attend(RequestAttendance requestDto, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);

        Week week = weekRepository.findByDate(LocalDate.now())
                .orElseThrow(WeekNotFoundException::new);

        if(week.isNotMatch(requestDto))
            throw new AttendanceCodeMismatchException();

        Attendance attendance = attendanceRepository.findAllByUserAndWeek(user, week)
                .orElseThrow(AttendanceNotFoundException::new);

        attendance.attend(true);
        user.attend();
        attendanceRepository.save(attendance);
    }

    //회원가입 승인과 연계할 메서드
    public void createAttendancesForUser(User user, Integer cardinal) {
        List<Week> weeks = weekRepository.findAllByCardinal(cardinal);
        for (Week week : weeks) {
            Attendance attendance = Attendance.builder()
                    .user(user)
                    .week(week)
                    .isAttend(false)
                    .build();

            attendanceRepository.save(attendance);
        }
    }

    public AttendanceDTO.Main find(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);

        Event event = eventRepository.findByTypeAndStartDateTimeIsBeforeAndEndDateTimeIsAfter(ATTENDANCE, LocalDateTime.now(), LocalDateTime.now())
                .orElseThrow(EventNotFoundException::new);

        return attendanceMapper.toMainDto(user, event);
    }

    public AttendanceDTO.Detail findAll(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);

        List<Event> events = eventRepository.findAllByTypeAndCardinal(ATTENDANCE, user.getCurrentCardinal());

        List<AttendanceDTO.Response> response = IntStream.range(0, events.size())
                .mapToObj(i -> attendanceMapper.toResponseDto(user.getAttendances().get(i), events.get(i)))
                .toList();

        return attendanceMapper.toDetailDto(user, response);
    }
}
