package leets.weeth.global.common.error.exception.custom;

public class InvalidAccessException extends BusinessLogicException {
    public InvalidAccessException() {
        super("잘못된 접근입니다.");
    }
}