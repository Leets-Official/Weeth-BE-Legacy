package leets.weeth.domain.attendance.controller;

import leets.weeth.domain.attendance.dto.RequestAttendance;
import leets.weeth.domain.attendance.dto.ResponseAttendance;
import leets.weeth.domain.attendance.service.AttendanceService;
import leets.weeth.global.auth.annotation.CurrentUser;
import leets.weeth.global.common.response.CommonResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/attendance")
@RequiredArgsConstructor
public class AttendanceController {

    private final AttendanceService attendanceService;

    @PostMapping
    public CommonResponse<ResponseAttendance> recordAttendance(@RequestBody @Validated RequestAttendance requestDto, @CurrentUser Long userId) {
        ResponseAttendance responseDto = attendanceService.recordAttendance(requestDto, userId);
        return CommonResponse.createSuccess(responseDto);
    }
}
