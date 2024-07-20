package leets.weeth.domain.admin.attendanceEvent.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import leets.weeth.domain.admin.attendanceEvent.dto.RequestAttendanceEvent;
import leets.weeth.domain.admin.attendanceEvent.service.AttendanceEventService;
import leets.weeth.global.auth.annotation.CurrentUser;
import leets.weeth.global.common.response.CommonResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static leets.weeth.domain.admin.attendanceEvent.enums.ResponseMessage.*;
@Tag(name = "Admin-AttendanceEventController", description = "출석일정을 관리하는 어드민 전용 API입니다.")
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/attendanceEvent")
public class AttendanceEventController {
    private final AttendanceEventService attendanceEventService;

    @Operation(summary = "출석 일정 생성", description = "관리자가 출석일정을 등록합니다.")
    @PostMapping("/create")
    public CommonResponse<String> createAttendanceEvent(@RequestBody @Valid RequestAttendanceEvent requestAttendanceEvent,
                                                        @Parameter(hidden = true) @CurrentUser Long userId) {
        attendanceEventService.createAttendanceEvent(requestAttendanceEvent, userId);
        return CommonResponse.createSuccess(ATTENDANCE_EVENT_CREATED_SUCCESS.getMessage());
    }
}
