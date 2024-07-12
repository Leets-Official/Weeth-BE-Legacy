package leets.weeth.domain.post.service;

import jakarta.transaction.Transactional;
import leets.weeth.domain.post.dto.PostDTO;
import leets.weeth.domain.post.entity.Post;
import leets.weeth.domain.post.repository.PostRepository;
import leets.weeth.domain.user.entity.User;
import leets.weeth.domain.user.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;


@Slf4j
@Service //서비스 객체 생성
public class PostService {
    @Autowired
    private PostRepository postRepository;    // 게시글 레파지터리 객체
    @Autowired
    private UserRepository userRepository;

    public List<PostDTO> index() {
        List<Post> posts = postRepository.findAll();
        return posts.stream()
                .map(PostDTO::createPostDTO)
                .collect(Collectors.toList());
    }

    public Post show(Long postId) {
        Post target = postRepository.findById(postId)
                .orElseThrow(()->new EntityNotFoundException("Failed to edit the Post. no such post."));
        return target;
    }


    public void create(String email, PostDTO dto) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(()->new UsernameNotFoundException("failed to add post! no such user"));
        Post post = Post.createPost(dto, user);
        postRepository.save(post);
    }

    @Transactional
    public void update(Long postId, PostDTO dto, String currentEmail) {

        Post target = postRepository.findById(postId)
                .orElseThrow(()->new EntityNotFoundException("Failed to edit the Post. no such post."));
        if (!target.getUser().getEmail().equals(currentEmail)) {
            throw new AccessDeniedException("You do not have permission to edit this post");
        }
        target.patch(dto);
        // 2. post 수정
        postRepository.save(target);
        // 3. DB로 갱신

    }
    @Transactional
    public void delete(Long postid, String currentEmail) {
        Post target = postRepository.findById(postid)
                .orElseThrow(()->new EntityNotFoundException("Failed to delete the Post. no such post"));
        if(!target.getUser().getEmail().equals(currentEmail)){
            throw new AccessDeniedException("You do not have permission to delete this post");
        }
        postRepository.delete(target);
    }
}
