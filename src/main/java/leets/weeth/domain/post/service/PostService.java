package leets.weeth.domain.post.service;

import org.springframework.transaction.annotation.Transactional;
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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Service //서비스 객체 생성
public class PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final FileService fileService;

    //모든 게시물 가져오기
    public List<ResponsePostDTO> findAllPosts() {
        // 모든 게시물을 id에 대해 오름차순으로 조회
        List<Post> posts = postRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
        return posts.stream()
                .map(ResponsePostDTO::createResponsePostDTO)
                .toList();
    }

    // 특정 postId의 게시물만 조회
    public ResponsePostDTO show(Long postId) {
        Post target = postRepository.findById(postId)
                .orElseThrow(PostNotFoundException::new);

        return ResponsePostDTO.createResponsePostDTO(target);
    }

    // 특정 유저(본인)의 게시물만 조회
    public List<ResponsePostDTO> myPosts(Long userId){
        // 특정 유저의 모든 게시물을 오름차순으로 조회
        List<Post> myPosts = postRepository.findByUserId(userId, Sort.by(Sort.Direction.ASC, "id"));

        // Post 리스트를 ResponsePostDTO 리스트로 변환
        return myPosts.stream()
                .map(ResponsePostDTO::createResponsePostDTO) // Post -> ResponsePostDTO 변환
                .toList();
    }

    @Transactional(rollbackFor = Exception.class)
    public void create(Long userId, RequestPostDTO requestPostDTO, List<MultipartFile> files, Long postId) throws InvalidAccessException {
        // 사용자가 존재하지 않는 경우
        User user = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);
        //
        List<String> fileUrls;
        Post newPost;
        if(postId!=null){
            Post targetPost = postRepository.findById(postId).orElse(null);
            // 대상 게시물이 존재하지 않는 경우
            if (targetPost==null){
                throw new PostNotFoundException();
            }
            // 게시글을 수정하려는 유저가 원래의 게시글 작성자와 다를 경우
            if(!(targetPost.getUser().getId() == userId)){
                throw new InvalidAccessException();
            }
            // 파일 첨부
            fileUrls = fileService.uploadFiles(files);
            newPost = postRepository.findById(postId).orElse(null);
            // 게시물 수정
            newPost.updatePost(requestPostDTO, fileUrls);
        }
        else {
            // 파일 첨부
            fileUrls = fileService.uploadFiles(files);
            // 게시물 생성
            newPost = Post.createPost(requestPostDTO, user, fileUrls);
        }
        postRepository.save(newPost);
    }

    @Transactional(rollbackFor = Exception.class)
    public void delete(Long postId, Long userId) throws InvalidAccessException {
        // 대상 게시물이 존재하지 않는 경우
        Post deleted = postRepository.findById(postId)
                .orElseThrow(PostNotFoundException::new);
        // 게시글을 수정하려는 유저가 원래의 게시글 작성자와 다를 경우
        if(!(deleted.getUser().getId() == userId)){
            throw new InvalidAccessException();
        }
        postRepository.delete(deleted);
    }

    public List<ResponsePostDTO> loadPosts(Long lastPostId) throws InvalidAccessException {
        Long maxPostId = postRepository.findMaxPostId();

        if(lastPostId==null){   // 첫번째 요청인 경우
            lastPostId = maxPostId + 1;
        }
        if(lastPostId <= 1 || lastPostId > maxPostId + 1){
            throw new InvalidAccessException(); // postId가 1 이하이거나 최대값보다 클경우
        }

        Pageable pageable = PageRequest.of(0, 15); // 첫 페이지, 페이지당 15개 게시글
        List<Post> posts = postRepository.findRecentPosts(lastPostId, pageable);
        return posts.stream()
                .map(ResponsePostDTO::createResponsePostDTO)
                .toList();
    }

}
