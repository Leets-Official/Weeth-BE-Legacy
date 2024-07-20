package leets.weeth.domain.notice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import leets.weeth.domain.notice.dto.RequestNotice;
import leets.weeth.domain.notice.dto.ResponseNotice;
import leets.weeth.domain.notice.service.NoticeService;
import leets.weeth.global.auth.annotation.CurrentUser;
import leets.weeth.global.common.error.exception.custom.BusinessLogicException;
import leets.weeth.global.common.error.exception.custom.TypeNotMatchException;
import leets.weeth.global.common.response.CommonResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static leets.weeth.domain.notice.enums.ResponseMessage.*;

@Tag(name = "NoticeController", description = "공지 관련 API입니다.")
@RestController
@RequiredArgsConstructor
@RequestMapping("/notice")
public class NoticeController {
    private final NoticeService noticeService;

    @Operation(summary = "공지 생성", description = "관리자가 공지사항을 등록합니다.")
    @PostMapping("/create")
    public CommonResponse<String> createNotice(@RequestPart(value = "requestNotice") @Valid RequestNotice requestNotice,
                                               @RequestPart(value = "files", required = false) List<MultipartFile> files,
                                               @Parameter(hidden = true) @CurrentUser Long userId) throws BusinessLogicException {
        noticeService.createNotice(requestNotice, files, userId);
        return CommonResponse.createSuccess(NOTICE_CREATED_SUCCESS.getMessage());
    }

    @Operation(summary = "공지사항 상세 조회", description = "사용자가 공지사항을 조회합니다.")
    @GetMapping("/{noticeId}")
    public CommonResponse<ResponseNotice> getNotice(@PathVariable Long noticeId) throws TypeNotMatchException {
        ResponseNotice responseNotice = noticeService.getNoticeById(noticeId);
        return CommonResponse.createSuccess(responseNotice);
    }

    @Operation(summary = "전체 공지사항 조회", description = "사용자가 전체 공지사항을 조회합니다.")
    @GetMapping("/")
    public CommonResponse<List<ResponseNotice>> getAllNotices() {
        List<ResponseNotice> responseNotices = noticeService.getAllNotices();
        return CommonResponse.createSuccess(responseNotices);
    }

    @Operation(summary = "공지사항 수정", description = "관리자가 공지사항을 수정합니다.")
    @PatchMapping("/{noticeId}")
    public CommonResponse<String> updateNotice(@RequestPart(value = "requestNotice") @Valid RequestNotice requestNotice,
                                               @RequestPart(value = "files", required = false) List<MultipartFile> files,
                                               @Parameter(hidden = true) @CurrentUser Long userId,
                                               @PathVariable Long noticeId) throws BusinessLogicException {
        noticeService.updateNotice(noticeId, requestNotice, files, userId);
        return CommonResponse.createSuccess(NOTICE_UPDATED_SUCCESS.getMessage());
    }

    @Operation(summary = "공지사항 삭제", description = "관리자가 공지사항을 삭제합니다.")
    @DeleteMapping("/{noticeId}")
    public CommonResponse<String> deleteNotice(@PathVariable Long noticeId, @Parameter(hidden = true) @CurrentUser Long userId) throws BusinessLogicException {
        noticeService.deleteNotice(noticeId, userId);
        return CommonResponse.createSuccess(NOTICE_DELETED_SUCCESS.getMessage());
    }
}