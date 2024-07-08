package leets.weeth.domain.user.mapper;

import leets.weeth.domain.user.dto.RequestEvent;
import leets.weeth.domain.user.dto.ResponseEvent;
import leets.weeth.domain.user.entity.Event;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface EventMapper {
    EventMapper INSTANCE = Mappers.getMapper(EventMapper.class);

    Event fromDto(RequestEvent requestEvent);

    ResponseEvent toDto(Event event);
}
