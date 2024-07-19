package leets.weeth.domain.notice.mapper;

import leets.weeth.domain.event.entity.Event;
import leets.weeth.domain.notice.dto.RequestNotice;
import leets.weeth.domain.notice.dto.ResponseNotice;
import leets.weeth.domain.user.entity.User;
import org.mapstruct.*;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface NoticeMapper {
    @Mappings({
            @Mapping(source = "user.name", target = "userName"),
            @Mapping(source = "createdAt", target = "createdAt"),
            @Mapping(source = "modifiedAt", target = "modifiedAt"),
    })
    ResponseNotice toNoticeDto(Event event);

    @Mappings({
            @Mapping(source = "user", target = "user"),
            @Mapping(target = "location", expression = "java(null)"),
            @Mapping(target = "startDateTime", expression = "java( java.time.LocalDateTime.now())"),
            @Mapping(target = "endDateTime", expression = "java( java.time.LocalDateTime.now())"),
            @Mapping(target = "type", expression = "java( leets.weeth.domain.event.entity.enums.Type.NOTICE)"),
            @Mapping(target = "id", ignore = true)
    })
    Event fromNoticeDto(RequestNotice dto, User user);
}
