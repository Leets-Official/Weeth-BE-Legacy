package leets.weeth.domain.post.service;

import jakarta.transaction.Transactional;
import leets.weeth.domain.post.dto.PostFileUploadDTO;
import leets.weeth.domain.post.dto.RequestPostDTO;
import leets.weeth.domain.post.dto.ResponsePostDTO;
import leets.weeth.domain.post.entity.Post;
import leets.weeth.domain.post.entity.PostFile;
import leets.weeth.domain.post.repository.PostFileRepository;
import leets.weeth.domain.post.repository.PostRepository;
import leets.weeth.domain.user.entity.User;
import leets.weeth.domain.user.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


@Slf4j
@Service //서비스 객체 생성
public class PostService {
    @Autowired
    private PostRepository postRepository;    // 게시글 레파지터리 객체
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PostFileRepository postFileRepository;
    @Value("${file.upload-dir}")
    private String uploadDir;


    //모든 게시물 가져오기
    public List<ResponsePostDTO> findAllPosts() {
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

    public List<ResponsePostDTO> myPosts(String email){
        List<Post> myPosts = postRepository.findByUserEmail(email, Sort.by(Sort.Direction.ASC, "id"));

        // Post 리스트를 ResponsePostDTO 리스트로 변환
        return myPosts.stream()
                .map(ResponsePostDTO::createResponsePostDTO) // Post -> ResponsePostDTO 변환
                .collect(Collectors.toList());
    }

    @Transactional
    public void create(String email, RequestPostDTO requestPostDTO, PostFileUploadDTO imageDTO) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(()->new UsernameNotFoundException("failed to add post! no such user"));
        Post newPost = Post.createPost(requestPostDTO, user);
        postRepository.save(newPost);

        if (imageDTO.getFiles() != null && !imageDTO.getFiles().isEmpty()) {
            for (MultipartFile file : imageDTO.getFiles()) {
                UUID uuid = UUID.randomUUID();
                String imageFileName = uuid + "_" + file.getOriginalFilename();

                File destinationFile = new File(uploadDir + imageFileName);

                try {
                    file.transferTo(destinationFile);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                PostFile image = PostFile.builder()
                        .url("/postFiles/" + imageFileName)
                        .post(newPost)
                        .build();

                postFileRepository.save(image);
                newPost.getPostFiles().add(image);

            }
        }
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
