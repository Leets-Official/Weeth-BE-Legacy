package leets.weeth.domain.user.dto;

import lombok.Getter;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Getter
@Service
public class RequestEvent {
    private String title;

    private String content;

    private LocalDateTime startDateTime;

    private LocalDateTime endDateTime;
}
