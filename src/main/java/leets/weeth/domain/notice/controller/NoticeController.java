package leets.weeth.domain.notice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import leets.weeth.domain.notice.dto.RequestNotice;
import leets.weeth.domain.notice.dto.ResponseNotice;
import leets.weeth.domain.notice.service.NoticeService;
import leets.weeth.domain.post.dto.RequestPostDTO;
import leets.weeth.global.auth.annotation.CurrentUser;
import leets.weeth.global.common.error.exception.custom.BusinessLogicException;
import leets.weeth.global.common.error.exception.custom.TypeNotMatchException;
import leets.weeth.global.common.response.CommonResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
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

    // 공지사항 생성
    @Operation(summary = "공지 생성", description = "관리자가 공지사항을 등록합니다.")
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    @PostMapping("/create")
    public CommonResponse<String> createNotice(@RequestPart(value = "requestNotice") @Valid RequestNotice requestNotice,
                                               @RequestPart(value = "files", required = false) List<MultipartFile> files,
                                               @Parameter(hidden = true) @CurrentUser Long userId) throws BusinessLogicException {
        noticeService.createNotice(requestNotice, files, userId);
        return CommonResponse.createSuccess(NOTICE_CREATED_SUCCESS.getMessage());
    }

    // 공지사항 세부 조회
    @Operation(summary = "공지사항 상세 조회", description = "사용자가 공지사항을 조회합니다.")
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    @GetMapping("/{id}")
    public CommonResponse<ResponseNotice> getNotice(@PathVariable Long id) throws TypeNotMatchException {
        ResponseNotice responseNotice = noticeService.getNoticeById(id);
        return CommonResponse.createSuccess(responseNotice);
    }

    // 공지사항 전부
    @Operation(summary = "전체 공지사항 조회", description = "사용자가 전체 공지사항을 조회합니다.")
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    @GetMapping("/")
    public CommonResponse<List<ResponseNotice>> getAllNotices() {
        List<ResponseNotice> responseNotices = noticeService.getNotices();
        return CommonResponse.createSuccess(responseNotices);
    }

    // 공지사항 수정
    @Operation(summary = "공지사항 수정", description = "관리자가 공지사항을 수정합니다.")
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    @PatchMapping("/{id}")
    public CommonResponse<String> updateNotice(@PathVariable Long id,
                                               @RequestPart(value = "requestNotice") @Valid RequestNotice requestNotice,
                                               @RequestPart(value = "files", required = false) List<MultipartFile> files,
                                               @Parameter(hidden = true) @CurrentUser Long userId) throws BusinessLogicException {
        noticeService.updateNotice(id, requestNotice,files, userId);
        return CommonResponse.createSuccess(NOTICE_UPDATED_SUCCESS.getMessage());
    }

    // 공지사항 삭제
    @Operation(summary = "공지사항 삭제", description = "관리자가 공지사항을 삭제합니다.")
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public CommonResponse<String> deleteNotice(@PathVariable Long id, @Parameter(hidden = true) @CurrentUser Long userId) throws BusinessLogicException {
        noticeService.deleteNotice(id, userId);
        return CommonResponse.createSuccess(NOTICE_DELETED_SUCCESS.getMessage());
    }
}