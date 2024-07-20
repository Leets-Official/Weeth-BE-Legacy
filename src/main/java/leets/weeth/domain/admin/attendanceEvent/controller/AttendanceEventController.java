package leets.weeth.domain.admin.attendanceEvent.controller;

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

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/attendanceEvent")
public class AttendanceEventController {
    private final AttendanceEventService attendanceEventService;

    @PostMapping("/create")
    public CommonResponse<String> createAttendanceEvent(@RequestBody RequestAttendanceEvent requestAttendanceEvent, @CurrentUser Long userId) {
        attendanceEventService.createAttendanceEvent(requestAttendanceEvent, userId);
        return CommonResponse.createSuccess(ATTENDANCE_EVENT_CREATED_SUCCESS.getMessage());
    }
}
