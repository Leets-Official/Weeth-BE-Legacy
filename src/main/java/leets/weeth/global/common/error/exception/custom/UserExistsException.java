package leets.weeth.global.common.error.exception.custom;

import jakarta.persistence.EntityNotFoundException;

public class UserExistsException extends EntityNotFoundException {
    public UserExistsException() {
        super("존재하지 않는 유저입니다.");
    }
}