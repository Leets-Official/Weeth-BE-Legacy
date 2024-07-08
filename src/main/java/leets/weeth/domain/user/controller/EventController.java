package leets.weeth.domain.user.controller;

import leets.weeth.domain.user.dto.RequestEvent;
import leets.weeth.domain.user.service.EventService;
import leets.weeth.global.common.response.CommonResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/event")
public class EventController {
    private final EventService eventService;

    @PostMapping("/create")
    public CommonResponse<String> createEvent(@RequestBody RequestEvent requestEvent) {
        eventService.createEvent(requestEvent);
        return CommonResponse.createSuccess();
    }
}
