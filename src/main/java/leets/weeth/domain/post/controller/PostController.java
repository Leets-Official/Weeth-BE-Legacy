package leets.weeth.domain.post.controller;

import leets.weeth.domain.post.dto.RequestPostDTO;
import leets.weeth.domain.post.dto.ResponsePostDTO;
import leets.weeth.domain.post.service.PostService;
import leets.weeth.global.auth.annotation.CurrentUser;
import leets.weeth.global.common.error.exception.custom.InvalidAccessException;
import leets.weeth.global.common.response.CommonResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
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
                                         @CurrentUser Long userId, @PathVariable(required = false) Long postId) throws InvalidAccessException {

        postService.create(userId, requestPostDTO, files, postId);
        return CommonResponse.createSuccess();
    }

    @GetMapping("")
    public CommonResponse<List<ResponsePostDTO>> findAllPosts(){
        List<ResponsePostDTO> posts = postService.findAllPosts();
        return CommonResponse.createSuccess(posts);
    }

    @GetMapping("/myPosts")
    public CommonResponse<List<ResponsePostDTO>> showMyPost(@CurrentUser Long userId){
        List<ResponsePostDTO> myPost = postService.myPosts(userId);
        return CommonResponse.createSuccess(myPost);
    }

    @GetMapping("/{postId}")
    public CommonResponse<ResponsePostDTO> showPost(@PathVariable Long postId){
        ResponsePostDTO newPost = postService.show(postId);
        return CommonResponse.createSuccess(newPost);
    }

    @DeleteMapping("/{postId}")
    public CommonResponse<String> delete(@PathVariable Long postId, @CurrentUser Long userId) throws InvalidAccessException {
        postService.delete(postId, userId);
        return CommonResponse.createSuccess();
    }

}
