package leets.weeth.global.common.error.exception.custom;
import jakarta.persistence.EntityNotFoundException;

public class AttendanceNotFoundException extends EntityNotFoundException {
    public AttendanceNotFoundException() {super("출석 정보가 존재하지 않습니다.");}
}