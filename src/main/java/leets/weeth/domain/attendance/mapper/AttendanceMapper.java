package leets.weeth.domain.attendance.mapper;

import leets.weeth.domain.attendance.dto.AttendanceDTO;
import leets.weeth.domain.attendance.entity.Attendance;
import leets.weeth.domain.attendance.entity.Week;
import leets.weeth.domain.event.entity.Event;
import leets.weeth.domain.user.entity.User;
import org.mapstruct.*;

import java.util.List;
import java.util.Random;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AttendanceMapper {

    AttendanceDTO.Main toMainDto(User user, Event event);

    @Mapping(target = "weekNumber", expression = "java( attendance.getWeek().getWeekNumber() )")
    AttendanceDTO.Response toResponseDto(Attendance attendance, Event event);

    @Mappings({
            @Mapping(target = "attendanceCount", expression = "java( user.getAttendanceCount() )"),
            @Mapping(target = "total", expression = "java( user.getAttendances().size() )"),
            @Mapping(target = "absenceCount", expression = "java( user.getAbsenceCount() )"),
            @Mapping(target = "attendances", source = "attendances")
    })
    AttendanceDTO.Detail toDetailDto(User user, List<AttendanceDTO.Response> attendances);

    @Mappings({
            @Mapping(target = "attendanceCode", expression = "java( generateRandomCode() )"),
            @Mapping(target = "weekInfo", expression = "java( convertToWeekInfo(dto) )")
    })
    Week fromWeek(AttendanceDTO.Week dto);

    default String convertToWeekInfo(AttendanceDTO.Week dto) {
        return dto.weekNumber() + "주차";
    }

    default String generateRandomCode() {
        Random random = new Random();
        int randomNum = random.nextInt(9000) + 1000; // 1000 이상 9999 이하의 난수 생성
        return String.format("%04d", randomNum); // 4자리 수로 포맷팅하여 반환
    }
}
