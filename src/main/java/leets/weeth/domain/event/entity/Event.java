package leets.weeth.domain.event.entity;

import jakarta.persistence.*;
import leets.weeth.domain.event.dto.RequestEvent;
import leets.weeth.global.common.entity.BaseEntity;
import lombok.*;

import java.time.LocalDateTime;

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
    public void update(RequestEvent requestEvent) {
        this.title = requestEvent.getTitle();
        this.content = requestEvent.getContent();
        this.location = requestEvent.getLocation();
        this.startDateTime = requestEvent.getStartDateTime();
        this.endDateTime = requestEvent.getEndDateTime();
    }
}
