package leets.weeth.domain.attendance.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import leets.weeth.domain.attendance.dto.RequestAttendance;
import leets.weeth.domain.attendance.dto.ResponseAttendance;
import leets.weeth.domain.attendance.dto.ResponseAttendanceDetails;
import leets.weeth.domain.attendance.service.AttendanceService;
import leets.weeth.global.common.response.CommonResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.time.LocalDate;
import java.util.List;

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
            @AuthenticationPrincipal Long userId) {
        ResponseAttendance response = attendanceService.recordAttendance(requestDto, userId);
        return CommonResponse.createSuccess(response);
    }
    @Operation(summary = "출석 정보 조회", description = "사용자의 출석정보조회 시점까지 기록된 모든 출석 정보를 조회합니다.")
    @GetMapping("/details")
    public CommonResponse<List<ResponseAttendanceDetails>> getAllAttendanceDetails(
            @AuthenticationPrincipal Long userId) {
        List<ResponseAttendanceDetails> response = attendanceService.getAllAttendanceDetails(userId);
        return CommonResponse.createSuccess(response);
    }
}
