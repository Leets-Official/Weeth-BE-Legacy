package leets.weeth.domain.post.controller;

import leets.weeth.domain.post.dto.PostDTO;
import leets.weeth.domain.post.entity.Post;
import leets.weeth.domain.post.service.PostService;
import leets.weeth.global.auth.jwt.service.JwtService;
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
    @Autowired
    private JwtService tokenProvider;
    @PostMapping("/new")
    public ResponseEntity<Post> create(@RequestBody PostDTO dto, @AuthenticationPrincipal User user){
        Post created = postService.create(user.getUsername(), dto);
        return (created != null)?
                ResponseEntity.status(HttpStatus.OK).body(created):
                ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @GetMapping("")
    public List<Post> index(){
        return postService.index();
    }

    @GetMapping("/{postId}")
    public Post show(@PathVariable Long postId){
        return postService.show(postId);
    }

    @PatchMapping("/{postId}")
    public ResponseEntity<Post> edit(@PathVariable Long postId,@RequestBody PostDTO dto, @AuthenticationPrincipal User user){
        Post edited = postService.update(postId, dto, user.getUsername());
        return (edited != null)?
                ResponseEntity.status(HttpStatus.OK).body(edited):
                ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<Post> delete(@PathVariable Long postId, @AuthenticationPrincipal User user){
        Post deleted = postService.delete(postId, user.getUsername());
        return (deleted != null)?
                ResponseEntity.status(HttpStatus.OK).body(deleted):
                ResponseEntity.status(HttpStatus.BAD_REQUEST).build();

    }

}
