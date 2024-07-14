package leets.weeth.domain.attendance.service;

import leets.weeth.domain.attendance.dto.ResponseStatistics;
import leets.weeth.domain.attendance.repository.AttendanceRepository;
import leets.weeth.domain.user.entity.User;
import leets.weeth.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AttendanceStatisticsService {

    private final AttendanceRepository attendanceRepository;
    private final UserRepository userRepository;

    public ResponseStatistics getAttendanceStatistics(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));

        // 출석 기록에서 주차 목록을 가져옴
        List<Integer> weeks = attendanceRepository.findDistinctWeeks();
        long totalWeeks = weeks.size();
        long attendedWeeks = attendanceRepository.countByUserIdAndIsAttendTrue(userId);
        double attendanceRate = totalWeeks > 0 ? (double) attendedWeeks / totalWeeks * 100 : 0;

        ResponseStatistics statistics = new ResponseStatistics();
        statistics.setTotalAttendances(totalWeeks);
        statistics.setTotalAttended(attendedWeeks);
        statistics.setAttendanceRate(attendanceRate);

        return statistics;
    }

    public void updateAttendanceRate(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));

        // 출석 기록에서 주차 목록을 가져옴
        List<Integer> weeks = attendanceRepository.findDistinctWeeks();
        long totalWeeks = weeks.size();
        long attendedWeeks = attendanceRepository.countByUserIdAndIsAttendTrue(userId);
        double attendanceRate = totalWeeks > 0 ? (double) attendedWeeks / totalWeeks * 100 : 0;

//        user.setAttendanceRate(attendanceRate);
        userRepository.save(user);
    }
}