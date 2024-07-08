package leets.weeth.domain.event.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ResponseMessage {

    EVENT_CREATED_SUCCESS("일정 생성 성공."),
    EVENT_UPDATED_SUCCESS("일정 수정 성공."),
    EVENT_DELETED_SUCCESS("일정 삭제 성공.");

    private final String message;
}
