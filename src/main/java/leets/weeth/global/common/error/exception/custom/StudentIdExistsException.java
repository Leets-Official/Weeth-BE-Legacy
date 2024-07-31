package leets.weeth.global.common.error.exception.custom;

import jakarta.persistence.EntityNotFoundException;

public class StudentIdExistsException extends EntityNotFoundException {
    public StudentIdExistsException() {super("이미 가입된 학번입니다.");}
}
