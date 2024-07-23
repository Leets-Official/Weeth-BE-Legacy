package leets.weeth.global.common.error.exception.custom;

import jakarta.persistence.EntityNotFoundException;

public class AccountNotFoundException extends EntityNotFoundException {
    public AccountNotFoundException() {super("존재하지 않는 장부입니다.");}
}
