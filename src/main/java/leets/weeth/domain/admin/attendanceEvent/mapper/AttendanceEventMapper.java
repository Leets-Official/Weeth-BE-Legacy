package leets.weeth.domain.admin.attendanceEvent.mapper;

import leets.weeth.domain.admin.attendanceEvent.dto.RequestAttendanceEvent;
import leets.weeth.domain.event.entity.Event;
import leets.weeth.domain.user.entity.User;
import org.mapstruct.*;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AttendanceEventMapper {

    /*
        * 어드민/출석일정 controller로 들어온 출석일정 생성 요청
        * location은 현재 null로 설정 -> 차후 정기모임 장소로 수정 가능
        * 해당 controller로 온 요청은 type을 ATTENDANCE로만 저장하여 타입 침범 예방
     */
    @Mappings({
            @Mapping(source = "user", target = "user"),
            @Mapping(target = "location", expression = "java(null)"),
            @Mapping(target = "type", expression = "java( leets.weeth.domain.event.entity.enums.Type.ATTENDANCE)"),
            @Mapping(target = "id", ignore = true)
    })
    Event fromAttendanceEventDto(RequestAttendanceEvent dto, User user);
}
