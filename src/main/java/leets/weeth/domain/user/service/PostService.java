package leets.weeth.domain.user.service;

import jakarta.transaction.Transactional;
import leets.weeth.domain.user.dto.PostDTO;
import leets.weeth.domain.user.entity.Post;
import leets.weeth.domain.user.entity.User;
import leets.weeth.domain.user.repository.PostRepository;
import leets.weeth.domain.user.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service //서비스 객체 생성
public class PostService {
    @Autowired
    private PostRepository postRepository;    // 게시글 레파지터리 객체
    @Autowired
    private UserRepository userRepository;

    public List<Post> index() {
        return postRepository.findAll();
    }


    public PostDTO create(Long userId, PostDTO dto) {
        User user = userRepository.findById(userId).orElseThrow(()->new
                IllegalArgumentException("failed to add comments! no such article"));
        Post post = Post.createPost(dto, user);
        if(post.getId()!=null){
            return null;    //post 객체에 id가 존재한다면
        }
        Post created = postRepository.save(post);
        return PostDTO.createPostDTO(created);
    }
}
