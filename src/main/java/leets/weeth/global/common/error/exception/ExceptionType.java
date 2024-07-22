package leets.weeth.global.common.error.exception;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import leets.weeth.global.common.error.exception.custom.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.Optional;

@Getter
@RequiredArgsConstructor
public enum ExceptionType {
    ENTITY_NOT_FOUND(HttpStatus.BAD_REQUEST, EntityNotFoundException.class),
    ENTITY_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, EntityExistsException.class),
    BUSINESS_LOGIC_ERROR(HttpStatus.BAD_REQUEST, BusinessLogicException.class),
    TYPE_NOT_MATCH(HttpStatus.BAD_REQUEST, TypeNotMatchException.class),
    USER_NOT_MATCH(HttpStatus.BAD_REQUEST, UserNotMatchException.class),
    USER_NOT_FOUND(HttpStatus.BAD_REQUEST, UserNotFoundException.class),
    USER_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, UserExistsException.class),
    INVALID_ACCESS_EXCEPTION(HttpStatus.BAD_REQUEST, InvalidAccessException.class),
    NOTICE_NOT_FOUND(HttpStatus.BAD_REQUEST, NoticeNotFoundException.class),
    EVENT_NOT_FOUND(HttpStatus.BAD_REQUEST, EventNotFoundException.class),
    INVALID_INPUT_DATE(HttpStatus.BAD_REQUEST, InvalidInputDateException.class),
    NOTICE_TYPE_NOT_MATCH(HttpStatus.BAD_REQUEST, NoticeTypeNotMatchException.class),
    ATTENDANCE_EVENT_TYPE_NOT_MATCH(HttpStatus.BAD_REQUEST, AttendanceEventTypeNotMatchException.class);

    private final HttpStatus httpStatus;
    private final Class<? extends Exception> type;

    public static Optional<ExceptionType> findException(Exception ex) {
        return Arrays.stream(ExceptionType.values())
                .filter(e -> e.getType().equals(ex.getClass())) // 클래스 타입
                .findAny();
    }
}
