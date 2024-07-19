package leets.weeth.domain.attendance.service;

import leets.weeth.domain.attendance.dto.RequestAttendance;
import leets.weeth.domain.attendance.dto.ResponseAttendance;
import leets.weeth.domain.attendance.entity.Attendance;
import leets.weeth.domain.attendance.repository.AttendanceRepository;
import leets.weeth.domain.event.entity.Event;
import leets.weeth.domain.event.entity.enums.Type;
import leets.weeth.domain.event.repository.EventRepository;
import leets.weeth.domain.user.entity.User;
import leets.weeth.domain.user.repository.UserRepository;
import leets.weeth.global.common.error.exception.custom.BusinessLogicException;
import leets.weeth.global.common.error.exception.custom.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;

    //무작위 4자리 출석코드 생성 메서드
    private String generateRandomAttendanceCode() {
        Random random = new Random();
        int randomNum = random.nextInt(9000) + 1000; // 1000 이상 9999 이하의 난수 생성
        return String.format("%04d", randomNum); // 4자리 수로 포맷팅하여 반환
    }
    @SneakyThrows
    public ResponseAttendance recordAttendance(RequestAttendance requestDto, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);

        LocalDateTime now = LocalDateTime.now();
        //이벤트에서 ATTENDANCE 타입의 모든 데이터 가져오기
        List<Event> attendanceEvents = eventRepository.findAllByType(Type.ATTENDANCE, Sort.by(Sort.Order.asc("startDateTime")));

        //현재 시간에 해당하는 정기모임 조회
        Event currentEvent = attendanceEvents.stream()
                .filter(event -> now.isAfter(event.getStartDateTime()) && now.isBefore(event.getEndDateTime()))
                .findFirst()
                .orElseThrow(() -> new BusinessLogicException("현재 시간에 해당하는 정기모임이 없습니다."));

        //출석 코드 검증 로직
        String generatedCode = generateRandomAttendanceCode();
        if (!generatedCode.equals(requestDto.getAttendanceCode())) {
            throw new BusinessLogicException("잘못된 출석 코드입니다.");
        }

        //isAttend = true 출석 기록 생성
        Attendance attendance = Attendance.builder()
                .user(user)
                .attendanceCode(requestDto.getAttendanceCode())
                .isAttend(true)
                .startDateTime(currentEvent.getStartDateTime())
                .endDateTime(currentEvent.getEndDateTime())
                .attendanceDateTime(now)
                .build();

        attendanceRepository.save(attendance);

        //출석 응답 DTO
        ResponseAttendance responseDto = new ResponseAttendance();
        responseDto.setScheduleTitle(currentEvent.getTitle());
        responseDto.setScheduleDateTime(currentEvent.getStartDateTime().toString());
        responseDto.setScheduleLocation(currentEvent.getLocation());

        long totalAttendances = attendanceRepository.countByUserAndIsAttendTrue(user);
        responseDto.setAttendanceRate((int) (totalAttendances * 100 / attendanceEvents.size()));
        responseDto.setAttendanceDate(now);

        return responseDto;
    }
}
