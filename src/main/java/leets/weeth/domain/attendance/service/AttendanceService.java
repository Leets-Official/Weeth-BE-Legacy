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
import leets.weeth.global.common.error.exception.custom.AttendanceCodeMismatchException;
import leets.weeth.global.common.error.exception.custom.UserNotFoundException;
import leets.weeth.global.common.error.exception.custom.EventNotFoundException;
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
        int randomNum = random.nextInt(9000) + 1000; //1000 이상 9999 이하의 난수 생성
        return String.format("%04d", randomNum); //4자리 수로 포맷팅하여 반환
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
                .orElseThrow(EventNotFoundException::new);

        //출석 코드 검증 로직
        String generatedCode = generateRandomAttendanceCode();
        if (!generatedCode.equals(requestDto.getAttendanceCode())) {
            throw new AttendanceCodeMismatchException();
        }

        //출석 여부 업데이트
        List<Attendance> attendances = attendanceRepository.findAllByUserAndWeek(user, currentEvent.getWeek());


        //출석 응답 DTO
        ResponseAttendance responseDto = ResponseAttendance.builder()
                .scheduleTitle(currentEvent.getTitle())
                .scheduleDateTime(currentEvent.getStartDateTime().toString())
                .scheduleLocation(currentEvent.getLocation())
                .attendanceRate(calculateAttendanceRate(user))
                .attendanceDate(now)
                .build();

        return responseDto;
    }
    private void updateAttendances(List<Attendance> attendances) {
        for (Attendance attendance : attendances) {
            // 빌더 패턴을 사용하여 기존 엔티티의 상태를 업데이트
            Attendance updatedAttendance = attendance.toBuilder()
                    .isAttend(true) // 출석 상태를 true로 설정
                    .build();

            // 엔티티를 저장
            attendanceRepository.save(updatedAttendance);
        }
    }
    private int calculateAttendanceRate(User user) {
        long totalAttendances = attendanceRepository.countByUserAndIsAttendTrue(user);
        long totalEvents = eventRepository.countTotalByType(Type.ATTENDANCE);
        if (totalEvents == 0) return 0;
        return (int) ((totalAttendances * 100) / totalEvents);
    }
    public void generateAttendances(int totalWeeks, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);

        List<Event> attendanceEvents = eventRepository.findAllByType(Type.ATTENDANCE, Sort.by(Sort.Order.asc("startDateTime")));

        // 각 이벤트에 대해 주차를 기준으로 출석 객체 생성
        for (Event event : attendanceEvents) {
            for (int currentWeek = 1; currentWeek <= totalWeeks; currentWeek++) {
                String attendanceCode = generateRandomAttendanceCode();

                Attendance attendance = Attendance.builder()
                        .user(user)
                        .attendanceCode(attendanceCode)
                        .isAttend(false) //초기 상태는 결석으로 설정
                        .startDateTime(event.getStartDateTime())
                        .endDateTime(event.getEndDateTime())
                        .week(currentWeek) //주차 정보 설정
                        .build();

                attendanceRepository.save(attendance);
            }
        }
    }
}
