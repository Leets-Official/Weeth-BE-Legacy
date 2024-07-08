package leets.weeth.domain.event.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ResponseEvent {
    private Long id;

    private String title;

    private String content;

    private String location;

    private LocalDateTime startDateTime;

    private LocalDateTime endDateTime;
}
