package leets.weeth.domain.admin.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import leets.weeth.domain.account.dto.AccountDTO;
import leets.weeth.domain.account.dto.ReceiptDTO;
import leets.weeth.domain.account.service.AccountService;
import leets.weeth.domain.account.service.ReceiptService;
import leets.weeth.domain.attendance.dto.AttendanceDTO;
import leets.weeth.domain.attendance.dto.ResponseWeekCode;
import leets.weeth.domain.attendance.service.WeekService;
import leets.weeth.domain.event.attendanceEvent.dto.RequestAttendanceEvent;
import leets.weeth.domain.event.attendanceEvent.dto.ResponseAttendanceEvent;
import leets.weeth.domain.event.attendanceEvent.service.AttendanceEventService;
import leets.weeth.domain.event.dto.RequestEvent;
import leets.weeth.domain.event.service.EventService;
import leets.weeth.domain.notice.dto.RequestNotice;
import leets.weeth.domain.notice.service.NoticeService;
import leets.weeth.domain.penalty.dto.RequestPenalty;
import leets.weeth.domain.penalty.dto.ResponsePenalty;
import leets.weeth.domain.penalty.service.PenaltyService;
import leets.weeth.domain.user.dto.UserDTO;
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
    private final AccountService accountService;
    private final ReceiptService receiptService;
    private final WeekService weekService;
    private final PenaltyService penaltyService;

    /*
        Event 관련 admin api
     */
    @Operation(summary = "일정 생성", description = "관리자가 일정을 등록합니다.")
    @PostMapping("/event")
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
    @PostMapping("/notice")
    public CommonResponse<String> createNotice(@RequestPart(value = "requestNotice") @Valid RequestNotice requestNotice,
                                               @RequestPart(value = "files", required = false) List<MultipartFile> files,
                                               @Parameter(hidden = true) @CurrentUser Long userId) {
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
    @PostMapping("/attendance-event")
    public CommonResponse<String> createAttendanceEvent(@RequestBody @Valid RequestAttendanceEvent requestAttendanceEvent,
                                                        @Parameter(hidden = true) @CurrentUser Long userId) {
        attendanceEventService.createAttendanceEvent(requestAttendanceEvent, userId);
        return CommonResponse.createSuccess(ATTENDANCE_EVENT_CREATED_SUCCESS.getMessage());
    }

    @Operation(summary = "출석 일정 조회", description = "관리자가 출석일정을 조회합니다.")
    @GetMapping("/attendance-event")
    public CommonResponse<List<ResponseAttendanceEvent>> getAttendanceEvents() {
        return CommonResponse.createSuccess(attendanceEventService.getAttendanceEvents());
    }

    @Operation(summary = "주차 생성")
    @PostMapping("/attendances")
    public CommonResponse<Void> createWeek(@RequestBody @Valid AttendanceDTO.Week dto) {
        weekService.createWeeks(dto);
        return CommonResponse.createSuccess();
    }

    /*
        Attendance 관련 admin api
    */
    @Operation(summary = "주차별 출석 코드 조회", description = "특정 주차에 대한 출석 코드를 조회합니다.")
    @GetMapping("/attendances/{cardinal}")
    public CommonResponse<List<ResponseWeekCode>> getWeekCode(@PathVariable Integer cardinal) {
        return CommonResponse.createSuccess(weekService.getWeekCode(cardinal));
    }

    @Operation(summary = "패널티 부여")
    @PostMapping("/penalty")
    public CommonResponse<Void> assignPenalty(@RequestBody RequestPenalty requestPenalty) {
        penaltyService.assignPenalty(requestPenalty);
        return CommonResponse.createSuccess();
    }

    @Operation(summary = "패널티 삭제")
    @DeleteMapping("/penalty")
    public CommonResponse<Void> removePenalty(@RequestParam Long penaltyId) {
        penaltyService.removePenalty(penaltyId);
        return CommonResponse.createSuccess();
    }

    @Operation(summary = "모든 유저의 패널티 확인")
    @GetMapping("/penalty/all")
    public CommonResponse<List<ResponsePenalty>> showAllPenalty() {
        List<ResponsePenalty> allPenalties = penaltyService.getAllPenaltiesSortedByUserId();
        return CommonResponse.createSuccess(allPenalties);
    }

    /*
    유저 관련 admin api
     */
    @Operation(summary = "가입 신청 승인", description = "관리자의 회원 가입 승인")
    @PatchMapping("/users")
    public CommonResponse<Void> accept(@RequestParam Long userId) {
        userService.accept(userId);
        return CommonResponse.createSuccess();
    }

    @Operation(summary = "유저 추방")
    @DeleteMapping("/users")
    public CommonResponse<Void> ban(@RequestParam Long userId) {
        userService.ban(userId);
        return CommonResponse.createSuccess();
    }

    @Operation(summary = "관리자로 승격/강등", description = "role=ADMIN, USER")
    @PatchMapping("/users/{role}")
    public CommonResponse<Void> updateRole(@RequestParam Long userId, @PathVariable String role) {
        userService.update(userId, role);
        return CommonResponse.createSuccess();
    }

    @Operation(summary = "다음 기수도 이어서 진행")
    @PostMapping("/users/apply/{cardinal}")
    public CommonResponse<String> applyOB(@RequestParam Long userId, @PathVariable Integer cardinal) {
        userService.applyOB(userId, cardinal);
        return CommonResponse.createSuccess();
    }

    @Operation(summary = "어드민용 회원 조회")
    @GetMapping("/users/all")
    public CommonResponse<List<UserDTO.AdminResponse>> findAll() {
        return CommonResponse.createSuccess(userService.findAllByAdmin());
    }

    @Operation(summary = "회원 비밀번호 초기화")
    @PatchMapping("/users/reset")
    public CommonResponse<Void> resetPassword(@RequestParam Long userId) {
        userService.resetPassword(userId);
        return CommonResponse.createSuccess();
    }

    /*
    회비 관련 admin api
     */
    @Operation(summary = "회비 총 금액 기입", description = "돈 걷어서 총 금액만큼의 장부 생성")
    @PostMapping("/account")
    public CommonResponse<Void> initAccount(@RequestBody @Valid AccountDTO.Save dto) {
        accountService.init(dto);
        return CommonResponse.createSuccess();
    }

    @Operation(summary = "회비 사용 내역 기입")
    @PostMapping("/account/{cardinal}")
    public CommonResponse<Void> spend(@RequestPart @Valid ReceiptDTO.Spend dto, @PathVariable Integer cardinal, @RequestPart(value = "files", required = false) List<MultipartFile> files) {
        receiptService.spend(dto, cardinal, files);
        return CommonResponse.createSuccess();
    }

    @Operation(summary = "회비 사용 내역 취소")
    @DeleteMapping("/account/{receiptId}")
    public CommonResponse<Void> cancel(@PathVariable Long receiptId) {
        receiptService.cancel(receiptId);
        return CommonResponse.createSuccess();
    }
}
