package leets.weeth.domain.attendance.service;

import leets.weeth.domain.attendance.repository.AttendanceRepository;
import leets.weeth.domain.user.entity.User;
import leets.weeth.domain.user.repository.UserRepository;
import leets.weeth.global.common.error.exception.custom.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.service.GenericResponseService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AttendanceStatisticsService {

    private final AttendanceRepository attendanceRepository;
    private final UserRepository userRepository;
    private final GenericResponseService responseBuilder;

    public void updateAttendanceRate(Long userId) {
        User user = userRepository.findById(userId).
                orElseThrow(UserNotFoundException::new);

        long totalAttendances = attendanceRepository.countByUserAndIsAttendTrue(user);
        long totalAbsences = attendanceRepository.countByUserAndIsAttendFalse(user);
        long totalRecords = totalAttendances + totalAbsences;

        if (totalRecords > 0) {
            double attendanceRate = (double) totalAttendances / totalRecords * 100;
            double absenceRate = (double) totalAbsences / totalRecords * 100;

            userRepository.save(user);
        }
    }
}
