package leets.weeth.domain.admin.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import leets.weeth.domain.event.attendanceEvent.dto.RequestAttendanceEvent;
import leets.weeth.domain.event.attendanceEvent.service.AttendanceEventService;
import leets.weeth.domain.event.dto.RequestEvent;
import leets.weeth.domain.event.service.EventService;
import leets.weeth.domain.notice.dto.RequestNotice;
import leets.weeth.domain.notice.service.NoticeService;
import leets.weeth.domain.user.service.UserService;
import leets.weeth.global.auth.annotation.CurrentUser;
import leets.weeth.global.common.error.exception.custom.BusinessLogicException;
import leets.weeth.global.common.response.CommonResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static leets.weeth.domain.event.attendanceEvent.enums.ResponseMessage.ATTENDANCE_EVENT_CREATED_SUCCESS;
import static leets.weeth.domain.event.entity.enums.ResponseMessage.*;
import static leets.weeth.domain.notice.enums.ResponseMessage.*;

@Tag(name = "AdminController", description = "어드민 전용 API입니다.")
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {
    private final EventService eventService;
    private final NoticeService noticeService;
    private final AttendanceEventService attendanceEventService;
    private final UserService userService;

    /*
        Event 관련 admin api
     */
    @Operation(summary = "일정 생성", description = "관리자가 일정을 등록합니다.")
    @PostMapping("/event/create")
    public CommonResponse<String> createEvent(@RequestBody @Valid RequestEvent requestEvent,
                                              @Parameter(hidden = true) @CurrentUser Long userId) throws BusinessLogicException {
        eventService.createEvent(requestEvent, userId);
        return CommonResponse.createSuccess(EVENT_CREATED_SUCCESS.getMessage());
    }

    @Operation(summary = "일정 수정", description = "관리자가 일정을 수정합니다.")
    @PatchMapping("/event/{eventId}")
    public CommonResponse<String> updateEvent(@PathVariable Long eventId, @RequestBody RequestEvent requestEvent,
                                              @Parameter(hidden = true) @CurrentUser Long userId) throws BusinessLogicException {
        eventService.updateEvent(eventId, requestEvent, userId);
        return CommonResponse.createSuccess(EVENT_UPDATED_SUCCESS.getMessage());
    }

    @Operation(summary = "일정 삭제", description = "관리자가 일정을 삭제합니다.")
    @DeleteMapping("/event/{eventId}")
    public CommonResponse<String> deleteEvent(@PathVariable Long eventId,
                                              @Parameter(hidden = true) @CurrentUser Long userId) throws BusinessLogicException {
        eventService.deleteEvent(eventId, userId);
        return CommonResponse.createSuccess(EVENT_DELETED_SUCCESS.getMessage());
    }

    /*
        Notice 관련 admin api
     */
    @Operation(summary = "공지 생성", description = "관리자가 공지사항을 등록합니다.")
    @PostMapping("/notice/create")
    public CommonResponse<String> createNotice(@RequestPart(value = "requestNotice") @Valid RequestNotice requestNotice,
                                               @RequestPart(value = "files", required = false) List<MultipartFile> files,
                                               @Parameter(hidden = true) @CurrentUser Long userId) throws BusinessLogicException {
        noticeService.createNotice(requestNotice, files, userId);
        return CommonResponse.createSuccess(NOTICE_CREATED_SUCCESS.getMessage());
    }

    @Operation(summary = "공지사항 수정", description = "관리자가 공지사항을 수정합니다.")
    @PatchMapping("/notice/{noticeId}")
    public CommonResponse<String> updateNotice(@RequestPart(value = "requestNotice") @Valid RequestNotice requestNotice,
                                               @RequestPart(value = "files", required = false) List<MultipartFile> files,
                                               @Parameter(hidden = true) @CurrentUser Long userId,
                                               @PathVariable Long noticeId) throws BusinessLogicException {
        noticeService.updateNotice(noticeId, requestNotice, files, userId);
        return CommonResponse.createSuccess(NOTICE_UPDATED_SUCCESS.getMessage());
    }

    @Operation(summary = "공지사항 삭제", description = "관리자가 공지사항을 삭제합니다.")
    @DeleteMapping("/notice/{noticeId}")
    public CommonResponse<String> deleteNotice(@PathVariable Long noticeId, @Parameter(hidden = true) @CurrentUser Long userId) throws BusinessLogicException {
        noticeService.deleteNotice(noticeId, userId);
        return CommonResponse.createSuccess(NOTICE_DELETED_SUCCESS.getMessage());
    }

    /*
        AttendanceEvent 관련 admin api
     */
    @Operation(summary = "출석 일정 생성", description = "관리자가 출석일정을 등록합니다.")
    @PostMapping("/attendanceEvent/create")
    public CommonResponse<String> createAttendanceEvent(@RequestBody @Valid RequestAttendanceEvent requestAttendanceEvent,
                                                        @Parameter(hidden = true) @CurrentUser Long userId) {
        attendanceEventService.createAttendanceEvent(requestAttendanceEvent, userId);
        return CommonResponse.createSuccess(ATTENDANCE_EVENT_CREATED_SUCCESS.getMessage());
    }

    @Operation(summary = "가입 신청 승인", description = "관리자의 회원 가입 승인")
    @PatchMapping("/users")
    public CommonResponse<Void> accept(@RequestParam Long userId) {
        userService.accept(userId);
        return CommonResponse.createSuccess();
    }
}
