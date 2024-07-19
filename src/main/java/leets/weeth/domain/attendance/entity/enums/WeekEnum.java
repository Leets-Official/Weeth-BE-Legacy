package leets.weeth.domain.attendance.entity.enums;

import java.time.LocalDate;

public enum WeekEnum {
    WEEK_1(LocalDate.of(2023, 1, 1), LocalDate.of(2023, 1, 7)),
    WEEK_2(LocalDate.of(2023, 1, 8), LocalDate.of(2023, 1, 14)),
    WEEK_3(LocalDate.of(2023, 1, 15), LocalDate.of(2023, 1, 17)),
    WEEK_4(LocalDate.of(2023, 1, 18), LocalDate.of(2023, 1, 19));

    private final LocalDate startDate;
    private final LocalDate endDate;

    WeekEnum(LocalDate startDate, LocalDate endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }
}
