package leets.weeth.domain.post.controller;

import leets.weeth.domain.post.dto.RequestPostDTO;
import leets.weeth.domain.post.dto.ResponsePostDTO;
import leets.weeth.domain.post.service.PostService;
import leets.weeth.global.auth.annotation.CurrentUser;
import leets.weeth.global.common.response.CommonResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/posts")
public class PostController {
    @Autowired
    private final PostService postService;
    @PostMapping(value = {"/{postId}",""})
    public CommonResponse<String> createOrUpdate(@RequestPart(value = "requestPostDTO") RequestPostDTO requestPostDTO,
                                         @RequestPart(value = "files", required = false) List<MultipartFile> files,
                                         @CurrentUser Long userId, @PathVariable(required = false) Long postId){

        postService.create(userId, requestPostDTO, files, postId);
        return CommonResponse.createSuccess();
    }

    @GetMapping("")
    public CommonResponse<List<ResponsePostDTO>> findAllPosts(){
        List<ResponsePostDTO> posts = postService.findAllPosts();
        return CommonResponse.createSuccess(posts);
    }

    @GetMapping("/myPosts")
    public CommonResponse<List<ResponsePostDTO>> show(@AuthenticationPrincipal User user){
        List<ResponsePostDTO> myPost = postService.myPosts(user.getUsername());
        return CommonResponse.createSuccess(myPost);
    }

    @GetMapping("/{postId}")
    public CommonResponse<ResponsePostDTO> show(@PathVariable Long postId){
        ResponsePostDTO newPost = postService.show(postId);
        return CommonResponse.createSuccess(newPost);
    }

    @PatchMapping("/{postId}")
    public CommonResponse<String> edit(@PathVariable Long postId, @RequestBody RequestPostDTO requestPostDTO, @AuthenticationPrincipal User user){
        postService.update(postId, requestPostDTO, user.getUsername());
        return CommonResponse.createSuccess();
    }

    @DeleteMapping("/{postId}")
    public CommonResponse<String> delete(@PathVariable Long postId, @AuthenticationPrincipal User user){
        postService.delete(postId, user.getUsername());
        return CommonResponse.createSuccess();
    }

}
