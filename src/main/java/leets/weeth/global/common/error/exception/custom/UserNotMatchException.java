package leets.weeth.global.common.error.exception.custom;

public class UserNotMatchException extends BusinessLogicException{
    public UserNotMatchException() {super("생성한 사용자와 일치하지 않습니다.");}
}
