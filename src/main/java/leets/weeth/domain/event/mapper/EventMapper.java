package leets.weeth.domain.event.mapper;

import leets.weeth.domain.event.dto.RequestEvent;
import leets.weeth.domain.event.dto.ResponseEvent;
import leets.weeth.domain.event.entity.Event;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface EventMapper {
    EventMapper INSTANCE = Mappers.getMapper(EventMapper.class);

//    Event fromDto(RequestEvent requestEvent);

    @Mapping(source = "user.name", target = "userName") // User 객체의 name 필드를 userName 필드로 매핑
    ResponseEvent toDto(Event event);

}