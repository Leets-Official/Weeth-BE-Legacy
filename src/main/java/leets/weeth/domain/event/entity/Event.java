package leets.weeth.domain.event.entity;

import jakarta.persistence.*;
import leets.weeth.domain.event.dto.RequestEvent;
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

    private Boolean isAllDay;

    private LocalDateTime startDateTime;

    private LocalDateTime endDateTime;
//
//    @ManyToOne
//    @JoinColumn(name = "user_id", nullable = false)
//    private User user;

    // 일정 수정을 위한 메소드
    public void updateFromDto(RequestEvent dto) {
        Optional.ofNullable(dto.title()).ifPresent(title -> this.title = title);
        Optional.ofNullable(dto.content()).ifPresent(content -> this.content = content);
        Optional.ofNullable(dto.location()).ifPresent(location -> this.location = location);
        Optional.of(dto.isAllDay()).ifPresent(isAllDay -> this.isAllDay = isAllDay);// boolean으로 가져오기 때문에 null을 허용하지 않음
        Optional.ofNullable(dto.startDateTime()).ifPresent(startDateTime -> this.startDateTime = startDateTime);
        Optional.ofNullable(dto.endDateTime()).ifPresent(endDateTime -> this.endDateTime = endDateTime);
    }
}
