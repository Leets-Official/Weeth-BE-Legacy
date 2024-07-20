package leets.weeth.global.common.error.exception.custom;

public class AttendanceCodeMismatchException extends BusinessLogicException {
    public AttendanceCodeMismatchException() {super("출석 코드가 일치하지 않습니다.");}
}