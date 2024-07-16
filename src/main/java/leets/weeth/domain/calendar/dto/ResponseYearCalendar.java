package leets.weeth.domain.calendar.dto;

import leets.weeth.domain.event.dto.ResponseEvent;

import java.util.List;
import java.util.Map;

public record ResponseYearCalendar(
        Integer year, Map<Integer, List<ResponseEvent>> responseEvents
) {
}
