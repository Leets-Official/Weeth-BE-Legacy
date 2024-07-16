package leets.weeth.domain.calendar.controller;

import leets.weeth.domain.calendar.dto.ResponseMonthCalendar;
import leets.weeth.domain.calendar.dto.ResponseYearCalendar;
import leets.weeth.domain.calendar.service.CalendarService;
import leets.weeth.global.common.error.exception.custom.BusinessLogicException;
import leets.weeth.global.common.response.CommonResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/calendar")
public class CalendarController {
    private final CalendarService calendarService;

    @GetMapping("")
    public CommonResponse<ResponseMonthCalendar> getMonthEventByCalendar(@RequestParam int year, @RequestParam int month) {
        return CommonResponse.createSuccess(calendarService.getData(year, month));
    }

    @GetMapping("/all")
    public CommonResponse<ResponseYearCalendar> getYearEventByCalendar(@RequestParam int year) throws BusinessLogicException {
        return CommonResponse.createSuccess(calendarService.getDataYear(year));
    }
}
