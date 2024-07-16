package leets.weeth.domain.attendance.controller;

import leets.weeth.domain.attendance.dto.RequestAttendance;
import leets.weeth.domain.attendance.dto.ResponseAttendance;
import leets.weeth.domain.attendance.service.AttendanceService;
import leets.weeth.global.common.response.CommonResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/attendance")
@RequiredArgsConstructor
public class AttendanceController {

    private final AttendanceService attendanceService;

    @PostMapping
    public CommonResponse<ResponseAttendance> recordAttendance(@RequestBody @Validated RequestAttendance requestDto) {
        ResponseAttendance responseDto = attendanceService.recordAttendance(requestDto);
        return CommonResponse.createSuccess(responseDto);
    }
}
