package leets.weeth.domain.attendance.service;

import leets.weeth.domain.attendance.repository.WeekRepository;
import leets.weeth.domain.attendance.entity.Week;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class WeekService {
    private final WeekRepository weekRepository;

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

    private String generateRandomCode() {
        Random random = new Random();
        int randomNum = random.nextInt(9000) + 1000; // 1000 이상 9999 이하의 난수 생성
        return String.format("%04d", randomNum); // 4자리 수로 포맷팅하여 반환
    }
}
