package leets.weeth.global.common.error.exception.custom;

import jakarta.persistence.EntityNotFoundException;

public class EmailExistsException extends EntityNotFoundException {
    public EmailExistsException() {
        super("이미 사용 중인 이메일입니다.");
    }
}