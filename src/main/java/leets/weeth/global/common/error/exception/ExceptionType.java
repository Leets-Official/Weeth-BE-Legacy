package leets.weeth.global.common.error.exception;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import leets.weeth.global.common.error.exception.custom.BusinessLogicException;
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
    BUSINESS_LOGIC_ERROR(HttpStatus.BAD_REQUEST, BusinessLogicException.class);

    private final HttpStatus httpStatus;
    private final Class<? extends Exception> type;

    public static Optional<ExceptionType> findException(Exception ex) {
        return Arrays.stream(ExceptionType.values())
                .filter(e -> e.getType().equals(ex.getClass())) // 클래스 타입
                .findAny();
    }
}
