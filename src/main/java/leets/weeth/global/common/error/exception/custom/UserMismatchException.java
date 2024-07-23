package leets.weeth.global.common.error.exception.custom;

public class UserMismatchException extends BusinessLogicException  {
    public UserMismatchException() {
        super("사용자가 현재 사용자와 일치하지 않습니다.");
    }
}