package leets.weeth.domain.event.entity;

import jakarta.persistence.*;
import leets.weeth.domain.calendar.entity.Calendar;
import leets.weeth.domain.event.dto.RequestEvent;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id", nullable=false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="calendar_id", nullable=false)
    private Calendar calendar;

    // 정적 팩토리 메서드
    public static Event fromDto(RequestEvent dto, User user, Calendar calendar) {
        return Event.builder()
                .title(dto.title())
                .content(dto.content())
                .location(dto.location())
                .startDateTime(dto.startDateTime())
                .endDateTime(dto.endDateTime())
                .user(user)
                .calendar(calendar).build();
    }

    // 일정 수정을 위한 메소드
    public void updateFromDto(RequestEvent dto, Calendar updatedCalendar) {
        Optional.ofNullable(dto.title()).ifPresent(title -> this.title = title);
        Optional.ofNullable(dto.content()).ifPresent(content -> this.content = content);
        Optional.ofNullable(dto.location()).ifPresent(location -> this.location = location);
        Optional.ofNullable(dto.startDateTime()).ifPresent(startDateTime -> this.startDateTime = startDateTime);
        Optional.ofNullable(dto.endDateTime()).ifPresent(endDateTime -> this.endDateTime = endDateTime);
        Optional.ofNullable(updatedCalendar).ifPresent(calendar -> this.calendar = updatedCalendar);
    }


}
