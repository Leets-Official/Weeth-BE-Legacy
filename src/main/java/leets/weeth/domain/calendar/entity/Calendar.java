package leets.weeth.domain.calendar.entity;

import jakarta.persistence.*;
import lombok.*;

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

    public static Calendar fromDate(Integer year, Integer month) {
        return Calendar.builder()
                .year(year)
                .month(month)
                .build();
    }
}
