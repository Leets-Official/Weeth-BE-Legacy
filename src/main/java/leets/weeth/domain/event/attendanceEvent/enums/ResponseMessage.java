package leets.weeth.domain.event.attendanceEvent.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ResponseMessage {
    ATTENDANCE_EVENT_CREATED_SUCCESS("출석일정 생성 성공.");

    private final String message;
}
