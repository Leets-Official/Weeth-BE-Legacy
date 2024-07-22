package leets.weeth.domain.event.entity;

import jakarta.persistence.*;
import leets.weeth.domain.event.dto.RequestEvent;
import leets.weeth.domain.event.entity.enums.Type;
import leets.weeth.domain.file.entity.File;
import leets.weeth.domain.notice.dto.RequestNotice;
import leets.weeth.domain.user.entity.User;
import leets.weeth.global.common.entity.BaseEntity;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
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

    private String requiredItems;

    private String memberNumber;

    private LocalDateTime startDateTime;

    private LocalDateTime endDateTime;

    @Enumerated(EnumType.STRING)
    private Type type;

    @OneToMany(cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<File> fileUrls = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // 일정 수정을 위한 메소드
    public void updateFromEventDto(RequestEvent dto) {
        Optional.ofNullable(dto.title()).ifPresent(title -> this.title = title);
        Optional.ofNullable(dto.content()).ifPresent(content -> this.content = content);
        Optional.ofNullable(dto.location()).ifPresent(location -> this.location = location);
        Optional.ofNullable(dto.requiredItems()).ifPresent(requiredItems -> this.requiredItems = requiredItems);
        Optional.ofNullable(dto.memberNumber()).ifPresent(memberNumber -> this.memberNumber = memberNumber);
        Optional.ofNullable(dto.startDateTime()).ifPresent(startDateTime -> this.startDateTime = startDateTime);
        Optional.ofNullable(dto.endDateTime()).ifPresent(endDateTime -> this.endDateTime = endDateTime);
    }

    // 공지 수정을 위한 메소드
    public void updateFromNoticeDto(RequestNotice dto, List<File> fileUrlList) {
        Optional.ofNullable(dto.title()).ifPresent(title -> this.title = title);
        Optional.ofNullable(dto.content()).ifPresent(content -> this.content = content);
        Optional.ofNullable(fileUrlList).ifPresent(files -> {
            this.fileUrls.clear();
            this.fileUrls.addAll(files);
        });
        // 시간은 최초 생성한 시간으로 고정
    }

    public int getWeek() {
        return 0;
    }
}
