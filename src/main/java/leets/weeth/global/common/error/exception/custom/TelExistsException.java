package leets.weeth.global.common.error.exception.custom;

import jakarta.persistence.EntityNotFoundException;

public class TelExistsException extends EntityNotFoundException {
    public TelExistsException() {super("이미 가입된 전화번호 입니다.");}
}
