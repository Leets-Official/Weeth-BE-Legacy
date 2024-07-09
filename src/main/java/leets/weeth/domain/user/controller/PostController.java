package leets.weeth.domain.user.controller;

import jakarta.validation.Valid;
import leets.weeth.domain.user.dto.PostDTO;
import leets.weeth.domain.user.dto.UserDTO;
import leets.weeth.domain.user.entity.Post;
import leets.weeth.domain.user.service.PostService;
import leets.weeth.domain.user.service.UserService;
import leets.weeth.global.auth.jwt.service.JwtService;
import leets.weeth.global.common.response.CommonResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<Post> create(@RequestBody PostDTO dto, @RequestHeader("Authorization") String token){
        String jwtToken = token.substring(7);
        System.out.println("token : " + jwtToken);
        String userEmail = String.valueOf(tokenProvider.extractEmail(jwtToken));
        Post created = postService.create(userEmail, dto);
        return (created != null)?
                ResponseEntity.status(HttpStatus.OK).body(created):
                ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }



}
