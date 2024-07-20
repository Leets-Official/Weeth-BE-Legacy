package leets.weeth.domain.notice.mapper;

import leets.weeth.domain.event.entity.Event;
import leets.weeth.domain.file.entity.File;
import leets.weeth.domain.notice.dto.RequestNotice;
import leets.weeth.domain.notice.dto.ResponseNotice;
import leets.weeth.domain.user.entity.User;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface NoticeMapper {
    @Mappings({
            @Mapping(source = "user.name", target = "userName"),
            @Mapping(source = "createdAt", target = "createdAt"),
            @Mapping(source = "modifiedAt", target = "modifiedAt"),
    })
    ResponseNotice toNoticeDto(Event event);

    /*
     * 공지사항이 게시글에 표시될 땐 제목, 내용, 시간, 파일밖에 없음
     * 이를 일정에 게시할 땐 장소, 준비물, 참여인원은 null로 고정하는게 나을듯
     */
    @Mappings({
            @Mapping(source = "user", target = "user"),
            @Mapping(target = "location", expression = "java(null)"),
            @Mapping(target = "requiredItems", expression = "java(null)"),
            @Mapping(target = "memberNumber", expression = "java(null)"),
            @Mapping(target = "startDateTime", expression = "java( java.time.LocalDateTime.now())"),
            @Mapping(target = "endDateTime", expression = "java( java.time.LocalDateTime.now())"),
            @Mapping(target = "type", expression = "java( leets.weeth.domain.event.entity.enums.Type.NOTICE)"),
            @Mapping(target = "id", ignore = true)
    })
    Event fromNoticeDto(RequestNotice dto, List<File> fileUrls, User user);
}
