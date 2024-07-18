package leets.weeth.domain.event.entity;

import jakarta.persistence.*;
import leets.weeth.domain.event.dto.RequestEvent;
import leets.weeth.domain.event.entity.enums.Type;
import leets.weeth.domain.notice.dto.RequestNotice;
import leets.weeth.domain.user.entity.User;
import leets.weeth.global.common.entity.BaseEntity;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Optional;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@ToString
public class Event extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "event_id")
    private Long id;

    private String title;

    private String content;

    private String location;

    private LocalDateTime startDateTime;

    private LocalDateTime endDateTime;

    @Enumerated(EnumType.STRING)
    private Type type;

    // 파일관련

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id", nullable=false)
    private User user;

    // 일정 생성을 위한 정적 팩토리 메서드
    public static Event fromEventDto(RequestEvent dto, User user) {
        return Event.builder()
                .title(dto.title())
                .content(dto.content())
                .location(dto.location())
                .startDateTime(dto.startDateTime())
                .endDateTime(dto.endDateTime())
                .type(dto.type())
                .user(user)
                .build();
    }

    // 공지사항 생성을 위한 정적 팩토리 메서드
    public static Event fromNoticeDto(RequestNotice dto, Type type, User user, LocalDateTime now) {
        return Event.builder()
                .title(dto.title())
                .content(dto.content())
                .location(null)
                .startDateTime(now)
                .endDateTime(now)
                .type(type)
                .user(user)
                .build();
    }

    // 일정 수정을 위한 메소드
    public void updateFromEventDto(RequestEvent dto) {
        Optional.ofNullable(dto.title()).ifPresent(title -> this.title = title);
        Optional.ofNullable(dto.content()).ifPresent(content -> this.content = content);
        Optional.ofNullable(dto.location()).ifPresent(location -> this.location = location);
        Optional.ofNullable(dto.startDateTime()).ifPresent(startDateTime -> this.startDateTime = startDateTime);
        Optional.ofNullable(dto.endDateTime()).ifPresent(endDateTime -> this.endDateTime = endDateTime);
        Optional.ofNullable(dto.type()).ifPresent(type -> this.type = type);
    }

    // 공지 수정을 위한 메소드
    public void updateFromNoticeDto(RequestNotice dto, LocalDateTime now) {
        Optional.ofNullable(dto.title()).ifPresent(title -> this.title = title);
        Optional.ofNullable(dto.content()).ifPresent(content -> this.content = content);
        Optional.ofNullable(now).ifPresent(startDateTime -> this.startDateTime = now);
        Optional.ofNullable(now).ifPresent(endDateTime -> this.endDateTime = now);
    }
}
