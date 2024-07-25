package leets.weeth.domain.attendance.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import leets.weeth.domain.attendance.dto.AttendanceDTO;
import leets.weeth.domain.attendance.dto.RequestAttendance;
import leets.weeth.domain.attendance.service.AttendanceService;
import leets.weeth.global.auth.annotation.CurrentUser;
import leets.weeth.global.common.response.CommonResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "AttendanceController", description = "출석 관련 API")
@RestController
@RequestMapping("/attendances")
@RequiredArgsConstructor
public class AttendanceController {

    private final AttendanceService attendanceService;

    @Operation(summary = "출석 체크", description = "사용자가 출석 코드를 입력하여 출석합니다.")
    @PostMapping("/check-in")
    public CommonResponse<Void> checkIn(@RequestBody @Valid RequestAttendance requestDto, @CurrentUser Long userId) {
        attendanceService.attend(requestDto, userId);
        return CommonResponse.createSuccess();
    }

    @Operation(summary = "출석 메인 페이지 조회")
    @GetMapping
    public CommonResponse<AttendanceDTO.Main> find(@CurrentUser Long userId) {
        return CommonResponse.createSuccess(attendanceService.find(userId));
    }

    @Operation(summary = "출석 정보 조회", description = "사용자의 출석정보조회 시점까지 기록된 모든 출석 정보를 조회합니다.")
    @GetMapping("/details")
    public CommonResponse<AttendanceDTO.Detail> findAll(@CurrentUser Long userId) {
        return CommonResponse.createSuccess(attendanceService.findAll(userId));
    }
}
