package leets.weeth.domain.calendar.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
public class Calendar {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "calendar_id")
    private Long id;

    private Integer year;

    private Integer month;

    @OneToMany(mappedBy = "calendar")
    private List<EventCalendar> eventCalendars;

    public static Calendar fromDate(Integer year, Integer month) {
        return Calendar.builder()
                .year(year)
                .month(month)
                .build();
    }
}
