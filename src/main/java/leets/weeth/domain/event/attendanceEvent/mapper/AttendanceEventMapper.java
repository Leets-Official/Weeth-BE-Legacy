package leets.weeth.domain.event.attendanceEvent.mapper;

import leets.weeth.domain.attendance.dto.ResponseWeekCode;
import leets.weeth.domain.attendance.entity.Week;
import leets.weeth.domain.event.attendanceEvent.dto.RequestAttendanceEvent;
import leets.weeth.domain.event.attendanceEvent.dto.ResponseAttendanceEvent;
import leets.weeth.domain.event.entity.Event;
import leets.weeth.domain.user.entity.User;
import org.mapstruct.*;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AttendanceEventMapper {

    /*
     * 어드민/출석일정 controller로 들어온 출석일정 생성 요청
     * 해당 controller로 온 요청은 type을 ATTENDANCE로만 저장하여 타입 침범 예방
     */
    @Mappings({
            @Mapping(source = "user", target = "user"),
            @Mapping(target = "type", expression = "java( leets.weeth.domain.event.entity.enums.Type.ATTENDANCE)"),
            @Mapping(target = "id", ignore = true)
    })
    Event fromAttendanceEventDto(RequestAttendanceEvent dto, User user);


    @Mappings({
            @Mapping(target = "weekCode", source = "week"),
            @Mapping(target = "cardinal", source = "event.cardinal")
    })
    ResponseAttendanceEvent toAttendanceEventDto(Event event, Week week);

    default ResponseWeekCode toResponseWeekCode(Week week) {
        if (week == null) {
            return null;
        }
        return new ResponseWeekCode(week);
    }
}
