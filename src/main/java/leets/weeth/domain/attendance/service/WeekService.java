package leets.weeth.domain.attendance.service;

import leets.weeth.domain.attendance.entity.Week;
import leets.weeth.domain.attendance.repository.WeekRepository;
import leets.weeth.domain.event.entity.Event;
import leets.weeth.domain.event.entity.enums.Type;
import leets.weeth.domain.event.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class WeekService {

    private final WeekRepository weekRepository;
    private final EventRepository eventRepository;

    //임원이 총 주차 입력시 관련 정보 생성하는 메서드
    public void createWeeks(int totalWeeks, int cardinal) {
        for (int i = 1; i <= totalWeeks; i++) {
            Week week = Week.builder()
                    .attendanceCode(generateRandomCode())
                    .weekInfo("Week " + i)
                    .cardinal(cardinal)
                    .build();
            weekRepository.save(week);
        }
    }
    //4자리 랜덤 출석 코드
    private String generateRandomCode() {
        Random random = new Random();
        int randomNum = random.nextInt(9000) + 1000; // 1000 이상 9999 이하의 난수 생성
        return String.format("%04d", randomNum); // 4자리 수로 포맷팅하여 반환
    }
}
