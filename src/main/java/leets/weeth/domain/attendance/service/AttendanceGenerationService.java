package leets.weeth.domain.attendance.service;

import leets.weeth.domain.attendance.entity.Attendance;
import leets.weeth.domain.attendance.repository.AttendanceRepository;
import leets.weeth.domain.event.entity.Event;
import leets.weeth.domain.event.entity.enums.Type;
import leets.weeth.domain.event.repository.EventRepository;
import leets.weeth.domain.user.entity.User;
import leets.weeth.domain.user.repository.UserRepository;
import leets.weeth.global.common.error.exception.custom.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Random;

@Component
@RequiredArgsConstructor
public class AttendanceGenerationService {
    private final AttendanceRepository attendanceRepository;
    private final EventRepository eventRepository;

    //무작위 4자리 출석코드 생성 메서드
    private String generateRandomAttendanceCode() {
        Random random = new Random();
        int randomNum = random.nextInt(9000) + 1000; //1000 이상 9999 이하의 난수 생성
        return String.format("%04d", randomNum); //4자리 수로 포맷팅하여 반환
    }

    public void generateAttendancesForUser(User user) {
        List<Event> attendanceEvents = eventRepository.findAllByType(Type.ATTENDANCE, Sort.by(Sort.Order.asc("startDateTime")));

        for (Event event : attendanceEvents) {
            String attendanceCode = generateRandomAttendanceCode();

            Attendance attendance = Attendance.builder()
                    .user(user)
                    .attendanceCode(attendanceCode)
                    .isAttend(false)  // 초기 상태는 결석으로 설정
                    .startDateTime(event.getStartDateTime())
                    .endDateTime(event.getEndDateTime())
//                    .week(event.getWeek())  //Event 객체에서 주차 정보를 가져온다
                    .build();

            attendanceRepository.save(attendance);

        }
    }
}
