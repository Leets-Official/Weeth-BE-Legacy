package leets.weeth.domain.post.service;

import jakarta.transaction.Transactional;
import leets.weeth.domain.post.dto.RequestPostDTO;
import leets.weeth.domain.post.dto.ResponsePostDTO;
import leets.weeth.domain.post.entity.Post;
import leets.weeth.domain.post.repository.PostRepository;
import leets.weeth.domain.user.entity.User;
import leets.weeth.domain.user.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
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

    //모든 게시물 가져오기
    public List<ResponsePostDTO> index() {
        List<Post> posts = postRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
        return posts.stream()
                .map(ResponsePostDTO::createResponsePostDTO)
                .collect(Collectors.toList());
    }

    public ResponsePostDTO show(Long postId) {
        Post target = postRepository.findById(postId)
                .orElseThrow(()->new EntityNotFoundException("Failed to edit the Post. no such post."));

        return ResponsePostDTO.createResponsePostDTO(target);
    }


    public void create(String email, RequestPostDTO requestPostDTO) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(()->new UsernameNotFoundException("failed to add post! no such user"));
        Post newPost = Post.createPost(requestPostDTO, user);
        postRepository.save(newPost);
    }

    @Transactional
    public void update(Long postId, RequestPostDTO requestPostDTO, String currentEmail) {

        Post updated = postRepository.findById(postId)
                .orElseThrow(()->new EntityNotFoundException("Failed to edit the Post. no such post."));
        if (!updated.getUser().getEmail().equals(currentEmail)) {
            throw new AccessDeniedException("You do not have permission to edit this post");
        }
        updated.updatePost(requestPostDTO);
        // 2. post 수정
        postRepository.save(updated);
        // 3. DB로 갱신

    }
    @Transactional
    public void delete(Long postid, String currentEmail) {
        Post deleted = postRepository.findById(postid)
                .orElseThrow(()->new EntityNotFoundException("Failed to delete the Post. no such post"));
        if(!deleted.getUser().getEmail().equals(currentEmail)){
            throw new AccessDeniedException("You do not have permission to delete this post");
        }
        postRepository.delete(deleted);
    }
}
