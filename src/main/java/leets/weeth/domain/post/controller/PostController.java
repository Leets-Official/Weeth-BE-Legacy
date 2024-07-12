package leets.weeth.domain.post.controller;

import leets.weeth.domain.post.dto.PostDTO;
import leets.weeth.domain.post.entity.Post;
import leets.weeth.domain.post.service.PostService;
import leets.weeth.global.auth.jwt.service.JwtService;
import leets.weeth.global.common.response.CommonResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.userdetails.User;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/posts")
public class PostController {
    @Autowired
    private final PostService postService;
    @PostMapping("/new")
    public CommonResponse<String> create(@RequestBody PostDTO dto, @AuthenticationPrincipal User user){
        postService.create(user.getUsername(), dto);
        return CommonResponse.createSuccess();
    }

    @GetMapping("")
    public CommonResponse<List<PostDTO>> index(){

        List<PostDTO> posts = postService.index();
        return CommonResponse.createSuccess(posts);
    }

    @GetMapping("/{postId}")
    public CommonResponse<Post> show(@PathVariable Long postId){
        Post newPost = postService.show(postId);
        return CommonResponse.createSuccess(newPost);
    }

    @PatchMapping("/{postId}")
    public CommonResponse<String> edit(@PathVariable Long postId,@RequestBody PostDTO dto, @AuthenticationPrincipal User user){
        postService.update(postId, dto, user.getUsername());
        return CommonResponse.createSuccess();
    }

    @DeleteMapping("/{postId}")
    public CommonResponse<String> delete(@PathVariable Long postId, @AuthenticationPrincipal User user){
        postService.delete(postId, user.getUsername());
        return CommonResponse.createSuccess();

    }

}
