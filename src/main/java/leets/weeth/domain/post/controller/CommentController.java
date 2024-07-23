package leets.weeth.domain.post.controller;

import leets.weeth.domain.post.dto.RequestCommentDTO;
import leets.weeth.domain.post.service.CommentService;
import leets.weeth.global.auth.annotation.CurrentUser;
import leets.weeth.global.common.error.exception.custom.InvalidAccessException;
import leets.weeth.global.common.error.exception.custom.UserMismatchException;
import leets.weeth.global.common.response.CommonResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/posts/{postId}/comments")
public class CommentController {
    @Autowired
    private CommentService commentService;
    @PostMapping("")
    public CommonResponse<String> createComment(@PathVariable Long postId, @RequestBody RequestCommentDTO dto,
                                         @CurrentUser Long userId) {
        commentService.createComment(userId, postId, dto);
        return CommonResponse.createSuccess();
    }

    @PatchMapping("/{commentId}")
    public CommonResponse<String> updateComment(@PathVariable Long postId, @PathVariable Long commentId, @CurrentUser Long userId,
                                                @RequestBody RequestCommentDTO dto) throws UserMismatchException {
        commentService.updateComment(userId, postId, commentId, dto);
        return CommonResponse.createSuccess();
    }

    @DeleteMapping("/{commentId}")
    public CommonResponse<String> deleteComment(@PathVariable Long commentId, @CurrentUser Long userId) throws UserMismatchException {
        commentService.deleteComment(userId, commentId);
        return CommonResponse.createSuccess();
    }

}
