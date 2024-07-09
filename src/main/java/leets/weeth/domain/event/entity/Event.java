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

    private LocalDateTime startDateTime;

    private LocalDateTime endDateTime;

    // 일정 수정을 위한 메소드
    public void updateFromDto(RequestEvent dto) {
        Optional.ofNullable(dto.getTitle()).ifPresent(title -> this.title = title);
        Optional.ofNullable(dto.getContent()).ifPresent(content -> this.content = content);
        Optional.ofNullable(dto.getLocation()).ifPresent(location -> this.location = location);
        Optional.ofNullable(dto.getStartDateTime()).ifPresent(startDateTime -> this.startDateTime = startDateTime);
        Optional.ofNullable(dto.getEndDateTime()).ifPresent(endDateTime -> this.endDateTime = endDateTime);
    }
}
