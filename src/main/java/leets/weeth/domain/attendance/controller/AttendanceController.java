package leets.weeth.domain.attendance.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import leets.weeth.domain.attendance.dto.RequestAttendance;
import leets.weeth.domain.attendance.dto.RequestGenerateAttendances;
import leets.weeth.domain.attendance.dto.ResponseAttendance;
import leets.weeth.domain.attendance.service.AttendanceService;
import leets.weeth.global.common.response.CommonResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

@Tag(name = "AttendanceController", description = "출석 관련 API")
@RestController
@RequestMapping("/attendance")
@RequiredArgsConstructor
public class AttendanceController {

    private final AttendanceService attendanceService;

    @Operation(summary = "출석 체크", description = "사용자가 출석 코드를 입력하여 출석합니다.")
    @PostMapping("/check-in")
    public CommonResponse<ResponseAttendance> recordAttendance(
            @RequestBody @Valid RequestAttendance requestDto,
            @AuthenticationPrincipal Long user) {
        ResponseAttendance response = attendanceService.recordAttendance(requestDto, user);
        return CommonResponse.createSuccess(response);
    }
    @Operation(summary = "출석 객체 생성", description = "주차와 사용자 ID를 기반으로 출석 객체를 생성합니다.")
    @PostMapping("/generate")
    public void generateAttendances(@RequestBody @Valid RequestGenerateAttendances requestDto) {
        attendanceService.generateAttendances(requestDto.getWeeks(), requestDto.getUserId());
    }
}
