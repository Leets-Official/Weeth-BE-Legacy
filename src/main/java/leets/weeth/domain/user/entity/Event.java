package leets.weeth.domain.user.entity;

import jakarta.persistence.*;
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
    public void update(String title, String content, String location, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        this.title = title;
        this.content = content;
        this.location = location;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
    }
}
