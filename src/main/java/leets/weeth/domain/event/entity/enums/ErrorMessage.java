package leets.weeth.domain.event.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ErrorMessage {

    EVENT_NOT_FOUND("id에 해당하는 일정이 존재하지 않습니다."),
    USER_NOT_FOUND("존재하지 않는 사용자입니다."),
    USER_NOT_MATCH("일정과 사용자가 일치하지 않습니다."),
    INVALID_DATE("입력한 기간이 올바르지 않습니다.");
    private final String message;
}
