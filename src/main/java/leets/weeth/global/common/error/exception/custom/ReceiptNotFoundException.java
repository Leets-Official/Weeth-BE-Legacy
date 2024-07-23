package leets.weeth.global.common.error.exception.custom;


import jakarta.persistence.EntityExistsException;

public class ReceiptNotFoundException extends EntityExistsException {
    public ReceiptNotFoundException() { super("존재하지 않는 내역입니다.");}
}

