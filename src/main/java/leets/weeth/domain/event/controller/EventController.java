package leets.weeth.domain.event.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import leets.weeth.domain.event.dto.RequestEvent;
import leets.weeth.domain.event.dto.ResponseEvent;
import leets.weeth.domain.event.service.EventService;
import leets.weeth.global.auth.annotation.CurrentUser;
import leets.weeth.global.common.error.exception.custom.BusinessLogicException;
import leets.weeth.global.common.response.CommonResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

import static leets.weeth.domain.event.entity.enums.ResponseMessage.*;

@Tag(name = "EventController", description = "일정 관련 API입니다.")
@RestController
@RequiredArgsConstructor
@RequestMapping("calendar/events")
public class EventController {
    private final EventService eventService;

    @Operation(summary = "일정 생성", description = "관리자가 일정을 등록합니다.")
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    @PostMapping("/create")
    public CommonResponse<String> createEvent(@RequestBody @Valid RequestEvent requestEvent, @Parameter(hidden = true) @CurrentUser Long userId) throws BusinessLogicException {
        eventService.createEvent(requestEvent, userId);
        return CommonResponse.createSuccess(EVENT_CREATED_SUCCESS.getMessage());
    }


    @Operation(summary = "일정 상세 조회", description = "사용자가 일정의 세부사항을 조회합니다.")
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    @GetMapping("/{id}")
    public CommonResponse<ResponseEvent> getEvent(@PathVariable Long id) {
        ResponseEvent responseEvent = eventService.getEventById(id);
        return CommonResponse.createSuccess(responseEvent);
    }


    @Operation(summary = "기간 일정 조회", description = "사용자가 일정 기간의 세부사항을 조회합니다.")
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    @GetMapping("")
    public CommonResponse<List<ResponseEvent>> getEvents(
            @RequestParam(name = "start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(name = "end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) throws BusinessLogicException {

        List<ResponseEvent> responseEvents = eventService.getEventsBetweenDate(startDate, endDate);
        return CommonResponse.createSuccess(responseEvents);
    }


    @Operation(summary = "일정 수정", description = "관리자가 일정을 수정합니다.")
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    @PatchMapping("/{id}")
    public CommonResponse<String> updateEvent(@PathVariable Long id, @RequestBody RequestEvent requestEvent, @Parameter(hidden = true) @CurrentUser Long userId) throws BusinessLogicException {
        eventService.updateEvent(id, requestEvent, userId);
        return CommonResponse.createSuccess(EVENT_UPDATED_SUCCESS.getMessage());
    }


    @Operation(summary = "일정 삭제", description = "관리자가 일정을 삭제합니다.")
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public CommonResponse<String> deleteEvent(@PathVariable Long id, @Parameter(hidden = true) @CurrentUser Long userId) throws BusinessLogicException {
        eventService.deleteEvent(id, userId);
        return CommonResponse.createSuccess(EVENT_DELETED_SUCCESS.getMessage());
    }

}
