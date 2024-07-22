package leets.weeth.global.common.error.exception.custom;

public class AttendanceEventTypeNotMatchException extends TypeNotMatchException{
    public AttendanceEventTypeNotMatchException() {super("출석일정은 직접 수정할 수 없습니다.");}
}
