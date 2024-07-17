package leets.weeth.domain.post.service;

import jakarta.transaction.Transactional;
import leets.weeth.domain.file.entity.File;
import leets.weeth.domain.file.service.FileService;
import leets.weeth.domain.post.dto.RequestPostDTO;
import leets.weeth.domain.post.dto.ResponsePostDTO;
import leets.weeth.domain.post.entity.Post;
import leets.weeth.domain.post.repository.PostRepository;
import leets.weeth.domain.user.entity.User;
import leets.weeth.domain.user.repository.UserRepository;
import leets.weeth.global.common.error.exception.custom.InvalidAccessException;
import leets.weeth.global.common.error.exception.custom.PostNotFoundException;
import leets.weeth.global.common.error.exception.custom.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Slf4j
@Service //서비스 객체 생성
public class PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final FileService fileService;

    //모든 게시물 가져오기
    public List<ResponsePostDTO> findAllPosts() {
        List<Post> posts = postRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
        return posts.stream()
                .map(ResponsePostDTO::createResponsePostDTO)
                .collect(Collectors.toList());
    }

    public ResponsePostDTO show(Long postId) {
        Post target = postRepository.findById(postId)
                .orElseThrow(PostNotFoundException::new);

        return ResponsePostDTO.createResponsePostDTO(target);
    }

    public List<ResponsePostDTO> myPosts(Long userId){
        List<Post> myPosts = postRepository.findByUserId(userId, Sort.by(Sort.Direction.ASC, "id"));

        // Post 리스트를 ResponsePostDTO 리스트로 변환
        return myPosts.stream()
                .map(ResponsePostDTO::createResponsePostDTO) // Post -> ResponsePostDTO 변환
                .collect(Collectors.toList());
    }

    @Transactional
    public void create(Long userId, RequestPostDTO requestPostDTO, List<MultipartFile> files, Long postId) throws InvalidAccessException {
        User user = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);
        List<File> fileUrls = fileService.uploadFiles(files);
        Post newPost;
        if(postId!=null){
            Optional<Post> targetPost = postRepository.findById(postId);
            if (!targetPost.isPresent()){
                throw new PostNotFoundException();
            }
            newPost = Post.updatePost(requestPostDTO, user, fileUrls, postId);
        }
        else {
            newPost = Post.createPost(requestPostDTO, user, fileUrls);
        }
        Post save = postRepository.save(newPost);
        System.out.println("save.getFileUrls() = " + save.getFileUrls());
    }

    @Transactional
    public void delete(Long postId, Long userId) throws InvalidAccessException {
        Post deleted = postRepository.findById(postId)
                .orElseThrow(PostNotFoundException::new);
        if(!(deleted.getUser().getId() == userId)){
            throw new InvalidAccessException();
        }
        postRepository.delete(deleted);
    }
}
