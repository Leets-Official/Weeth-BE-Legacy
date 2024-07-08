package leets.weeth.domain.user.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Getter
@Setter
public class RequestEvent {
    private String title;

    private String content;

    private LocalDateTime startDateTime;

    private LocalDateTime endDateTime;
}
