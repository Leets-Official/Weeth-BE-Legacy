package leets.weeth.domain.attendance.entity.enums;

public enum Week {
    WEEK_1,
    WEEK_2,
    WEEK_3,
    WEEK_4;
    public static Week of(int weekNumber) {
        return Week.values()[weekNumber % Week.values().length];
    }
}
