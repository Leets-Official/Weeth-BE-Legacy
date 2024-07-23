package leets.weeth.global.common.error.exception.custom;


import jakarta.persistence.EntityExistsException;

public class AccountExistsException extends EntityExistsException {
    public AccountExistsException() { super("이미 생성된 장부입니다.");}
}

