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


    public Post create(String userEmail, PostDTO dto) {
        User user = userRepository.findByEmail(userEmail).orElseThrow(()->new
                IllegalArgumentException("failed to add comments! no such article"));
        Post post = Post.createPost(dto, user);
        if(post.getId()!=null){
            return null;    //post 객체에 id가 존재한다면
        }
        Post created = postRepository.save(post);
        return created;
    }

    @Transactional
    public PostDTO update(Long postid, PostDTO dto) {
        Post target = postRepository.findById(postid)
                .orElseThrow(()->new IllegalArgumentException("failed to edit the Post. so such post."));
        target.patch(dto);
        // 2. post 수정
        Post updated = postRepository.save(target);
        // 3. DB로 갱신
        return PostDTO.createPostDTO(updated);

    }
    @Transactional
    public PostDTO delete(Long postid) {
        Post target = postRepository.findById(postid)
                .orElseThrow(()->new IllegalArgumentException("no such post"));
        postRepository.delete(target);
        return  PostDTO.createPostDTO(target);
    }
}
