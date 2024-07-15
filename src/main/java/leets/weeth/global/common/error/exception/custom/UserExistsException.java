package leets.weeth.global.common.error.exception.custom;

import jakarta.persistence.EntityNotFoundException;

public class UserExistsException extends EntityNotFoundException {
    public UserExistsException() {
        super("이미 가입된 사용자입니다.");
    }
}