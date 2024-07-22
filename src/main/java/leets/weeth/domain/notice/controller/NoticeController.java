package leets.weeth.domain.notice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import leets.weeth.domain.notice.dto.ResponseNotice;
import leets.weeth.domain.notice.service.NoticeService;
import leets.weeth.global.common.error.exception.custom.TypeNotMatchException;
import leets.weeth.global.common.response.CommonResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "NoticeController", description = "공지 관련 API입니다.")
@RestController
@RequiredArgsConstructor
@RequestMapping("/notice")
public class NoticeController {
    private final NoticeService noticeService;

    @Operation(summary = "공지사항 상세 조회", description = "사용자가 공지사항을 조회합니다.")
    @GetMapping("/{noticeId}")
    public CommonResponse<ResponseNotice> getNotice(@PathVariable Long noticeId) throws TypeNotMatchException {
        ResponseNotice responseNotice = noticeService.getNoticeById(noticeId);
        return CommonResponse.createSuccess(responseNotice);
    }

    @Operation(summary = "전체 공지사항 조회", description = "사용자가 전체 공지사항을 조회합니다.")
    @GetMapping("")
    public CommonResponse<List<ResponseNotice>> getAllNotices() {
        List<ResponseNotice> responseNotices = noticeService.getAllNotices();
        return CommonResponse.createSuccess(responseNotices);
    }
}