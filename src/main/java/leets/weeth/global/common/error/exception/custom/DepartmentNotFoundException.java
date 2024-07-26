package leets.weeth.global.common.error.exception.custom;

import jakarta.persistence.EntityNotFoundException;

public class DepartmentNotFoundException extends EntityNotFoundException {
    public DepartmentNotFoundException() {super("존재하지 않는 학과입니다.");}
}
