package leets.weeth.domain.user.controller;

import leets.weeth.domain.user.dto.RequestEvent;
import leets.weeth.domain.user.dto.ResponseEvent;
import leets.weeth.domain.user.service.EventService;
import leets.weeth.global.common.response.CommonResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/events")
public class EventController {
    private final EventService eventService;

    // 일정 생성
    @PostMapping("/create")
    public CommonResponse<String> createEvent(@RequestBody RequestEvent requestEvent) {
        eventService.createEvent(requestEvent);
        return CommonResponse.createSuccess("일정 생성 성공.");
    }

    // 일정 상세 조회
    @GetMapping("/{id}")
    public CommonResponse<ResponseEvent> getEvent(@PathVariable Long id) {
        ResponseEvent responseEvent = eventService.getEventById(id);
        return CommonResponse.createSuccess(responseEvent);
    }

    // 기간 별 일정 조회
    @GetMapping("")
    public CommonResponse<List<ResponseEvent>> getEvents(
            @RequestParam(name = "start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(name = "end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {

        List<ResponseEvent> responseEvents = eventService.getEventsBetweenDate(startDate, endDate);
        return CommonResponse.createSuccess(responseEvents);
    }

    // 일정 수정
    @PatchMapping("/update/{id}")
    public CommonResponse<String> updateEvent(@PathVariable Long id, @RequestBody RequestEvent requestEvent) {
        eventService.updateEvent(id, requestEvent);
        return CommonResponse.createSuccess("id: " + id + " 일정 수정 성공");
    }

    // 일정 삭제
    @DeleteMapping("/{id}")
    public CommonResponse<String> deleteEvent(@PathVariable Long id) {
        eventService.deleteEvent(id);
        return CommonResponse.createSuccess("id: " + id + " 일정 삭제 성공");
    }

}
