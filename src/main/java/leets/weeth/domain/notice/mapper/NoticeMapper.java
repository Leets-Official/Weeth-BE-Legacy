package leets.weeth.domain.notice.mapper;

import leets.weeth.domain.event.entity.Event;
import leets.weeth.domain.notice.dto.ResponseNotice;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface NoticeMapper {
    NoticeMapper INSTANCE = Mappers.getMapper(NoticeMapper.class);

    @Mappings({
            @Mapping(source = "user.name", target = "userName"),
            @Mapping(source = "createdAt", target = "created_at"),
            @Mapping(source = "modifiedAt", target = "modified_at"),
    })
    ResponseNotice toDto(Event event);

}
