package leets.weeth.domain.calendar.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import leets.weeth.domain.calendar.dto.ResponseMonthData;
import leets.weeth.domain.calendar.dto.ResponseYearData;
import leets.weeth.domain.calendar.service.CalendarService;
import leets.weeth.global.common.error.exception.custom.BusinessLogicException;
import leets.weeth.global.common.response.CommonResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "CalendarController", description = "캘린더 관련 API입니다.")
@RestController
@RequiredArgsConstructor
@RequestMapping("/calendar")
public class CalendarController {
    private final CalendarService calendarService;

    @Operation(summary = "월별 캘린더 조회", description = "사용자가 1달 단위로 캘린더를 조회합니다.")
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    @GetMapping("")
    public CommonResponse<ResponseMonthData> getMonthData(@RequestParam int year, @RequestParam int month) {
        return CommonResponse.createSuccess(calendarService.getMonthDataByDate(year, month));
    }

    @Operation(summary = "년별 캘린더 조회", description = "사용자가 1년 단위로 캘린더를 조회합니다.")
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    @GetMapping("/all")
    public CommonResponse<ResponseYearData> getYearData(@RequestParam int year) throws BusinessLogicException {
        return CommonResponse.createSuccess(calendarService.getYearDataByDate(year));
    }
}
