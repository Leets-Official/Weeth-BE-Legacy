package leets.weeth.domain.attendance.entity.enums;

public enum Week {
    WEEK_1(1),
    WEEK_2(2),
    WEEK_3(3),
    WEEK_4(4);
    private final int weekNumber;

    Week(int weekNumber) {
        this.weekNumber = weekNumber;
    }

    public int getWeekNumber() {
        return weekNumber;
    }

    public static Week of(int week) {
        for (Week w : values()) {
            if (w.weekNumber == week) {
                return w;
            }
        }
        throw new IllegalArgumentException("Invalid week: " + week);
    }
}
