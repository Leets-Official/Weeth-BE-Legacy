package leets.weeth.domain.post.controller;

import leets.weeth.domain.post.dto.RequestCommentDTO;
import leets.weeth.domain.post.dto.ResponseCommentDTO;
import leets.weeth.domain.post.service.CommentService;
import leets.weeth.global.common.response.CommonResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/articles/{articleId}/comments")
public class CommentController {
    @Autowired
    private CommentService commentService;
    @PostMapping()
    public CommonResponse<String> create(@PathVariable Long articleId, @RequestBody RequestCommentDTO dto,
                                         @AuthenticationPrincipal User user){
        commentService.create(user.getUsername(), articleId, dto);
        return CommonResponse.createSuccess();
    }
    @GetMapping()
    public CommonResponse<List<ResponseCommentDTO>> show(@PathVariable Long articleId){
        List<ResponseCommentDTO> comments = commentService.comments(articleId);
        return CommonResponse.createSuccess(comments);
    }
}
