package leets.weeth.global.common.error.exception.custom;

public class WeekNotFoundException extends BusinessLogicException {
    public WeekNotFoundException() {super("존재하지 않는 주차정보입니다.");}
}
