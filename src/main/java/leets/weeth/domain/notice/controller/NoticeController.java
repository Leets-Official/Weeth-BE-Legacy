package leets.weeth.domain.notice.controller;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import leets.weeth.domain.notice.dto.RequestNotice;
import leets.weeth.domain.notice.dto.ResponseNotice;
import leets.weeth.domain.notice.service.NoticeService;
import leets.weeth.global.auth.annotation.CurrentUser;
import leets.weeth.global.common.error.exception.custom.BusinessLogicException;
import leets.weeth.global.common.response.CommonResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import static leets.weeth.domain.event.entity.enums.ResponseMessage.*;

@Tag(name = "NoticeController", description = "공지 관련 API입니다.")
@RestController
@RequiredArgsConstructor
@RequestMapping("notice")
public class NoticeController {
    private final NoticeService noticeService;

    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    @PostMapping("/create")
    public CommonResponse<String> createNotice(@RequestBody @Valid RequestNotice requestNotice, @Parameter(hidden = true) @CurrentUser Long userId) throws BusinessLogicException {
        noticeService.createNotice(requestNotice, userId);
        return CommonResponse.createSuccess(EVENT_CREATED_SUCCESS.getMessage());
    }

    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    @GetMapping("/{id}")
    public CommonResponse<ResponseNotice> getNotice(@PathVariable Long id) {
        ResponseNotice responseNotice = noticeService.getNoticeById(id);
        return CommonResponse.createSuccess(responseNotice);
    }

    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    @PatchMapping("/{id}")
    public CommonResponse<String> updateNotice(@PathVariable Long id, @RequestBody RequestNotice requestNotice, @Parameter(hidden = true) @CurrentUser Long userId) throws BusinessLogicException {
        noticeService.updateNotice(id, requestNotice, userId);
        return CommonResponse.createSuccess(EVENT_UPDATED_SUCCESS.getMessage());
    }


    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public CommonResponse<String> deleteNotice(@PathVariable Long id, @Parameter(hidden = true) @CurrentUser Long userId) throws BusinessLogicException {
        noticeService.deleteNotice(id, userId);
        return CommonResponse.createSuccess(EVENT_DELETED_SUCCESS.getMessage());
    }
}