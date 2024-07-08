package leets.weeth.domain.event.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ErrorMessage {

    EVENT_NOT_FOUND("id에 해당하는 일정이 존재하지 않습니다.");

    private final String message;
}
