package leets.weeth.global.common.error.exception.custom;

import jakarta.persistence.EntityNotFoundException;

public class NoticeNotFoundException extends EntityNotFoundException {
    public NoticeNotFoundException() {super("존재하지 않는 공지사항입니다.");}
}
