package leets.weeth.domain.event.mapper;

import leets.weeth.domain.event.dto.RequestEvent;
import leets.weeth.domain.event.dto.ResponseEvent;
import leets.weeth.domain.event.entity.Event;
import leets.weeth.domain.user.entity.User;
import org.mapstruct.*;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface EventMapper {

    @Mappings({
            @Mapping(source = "user.name", target = "userName"),
            @Mapping(source = "createdAt", target = "createdAt"),
            @Mapping(source = "modifiedAt", target = "modifiedAt"),
    })
    ResponseEvent toEventDto(Event event);

    @Mappings({
            @Mapping(source = "user", target = "user"),
            @Mapping(target = "type", expression = "java( leets.weeth.domain.event.entity.enums.Type.EVENT)"),
            @Mapping(target = "id", ignore = true)
    })
    Event fromEventDto(RequestEvent dto, User user);
}