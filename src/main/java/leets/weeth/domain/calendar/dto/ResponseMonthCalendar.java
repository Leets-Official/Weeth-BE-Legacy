package leets.weeth.domain.calendar.dto;

import leets.weeth.domain.event.dto.ResponseEvent;

import java.util.List;

public record ResponseMonthCalendar(
        Integer year, Integer month, List<ResponseEvent> responseEvents
) {
}
