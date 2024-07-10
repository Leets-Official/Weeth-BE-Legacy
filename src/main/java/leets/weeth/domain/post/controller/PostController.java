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
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

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

}
