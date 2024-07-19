package leets.weeth.domain.post.service;

import leets.weeth.domain.post.dto.RequestCommentDTO;
import leets.weeth.domain.post.dto.ResponseCommentDTO;
import leets.weeth.domain.post.entity.Comment;
import leets.weeth.domain.post.entity.Post;
import leets.weeth.domain.post.repository.CommentRepository;
import leets.weeth.domain.post.repository.PostRepository;
import leets.weeth.domain.user.entity.User;
import leets.weeth.domain.user.repository.UserRepository;
import leets.weeth.global.common.error.exception.custom.CommentNotFoundException;
import leets.weeth.global.common.error.exception.custom.InvalidAccessException;
import leets.weeth.global.common.error.exception.custom.PostNotFoundException;
import leets.weeth.global.common.error.exception.custom.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class CommentService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;

    public List<ResponseCommentDTO> getAllCommentsFromPost(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(PostNotFoundException::new);

        // parent와 child를 포함한 모든 댓글
        List<Comment> allComments = commentRepository.findByPostId(postId);

        // 부모 댓글만 필터링
        List<Comment> parentComments = allComments.stream()
                .filter(comment -> comment.getParent() == null)
                .collect(Collectors.toList());

        // 부모 댓글을 ResponseCommentDTO로 변환하고, 자식 댓글을 계층적으로 설정
        return parentComments.stream()
                .map(ResponseCommentDTO::createResponseCommentDto)
                .collect(Collectors.toList());
    }

    public void create(Long userId, Long postId, RequestCommentDTO requestCommentDTO) throws InvalidAccessException {
        User user = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);
        Post createdPost = postRepository.findById(postId).orElseThrow(PostNotFoundException::new);
        
        Comment newComment = Comment.createComment(requestCommentDTO, createdPost, user);
        Comment parentComment;
        // child인 경우(부모가 있는 경우)
        if(requestCommentDTO.getParentId()!=null){
            parentComment = commentRepository.findById(requestCommentDTO.getParentId())
                    .orElseThrow(CommentNotFoundException::new);
            // 부모의 부모는 없어야함(최대 1회만 참조 가능)
            if(parentComment.getParent()!=null){
                throw new InvalidAccessException();
            }
            //부모 comment 설정
            newComment.setParentComment(parentComment);
        }
        commentRepository.save(newComment);
    }

    public void updateComment(Long userId, Long postId, Long commentId, RequestCommentDTO requestCommentDTO) throws InvalidAccessException {
        User user = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);
        Post post = postRepository.findById(postId)
                .orElseThrow(PostNotFoundException::new);
        Comment editedComment = commentRepository.findById(commentId)
                .orElseThrow(CommentNotFoundException::new);
        
        // 댓글을 쓴 유저와 수정하는 유저가 다른 경우
        if(user.getId()!=editedComment.getUser().getId()){
            throw new InvalidAccessException();
        }
        editedComment.updateComment(requestCommentDTO);
        commentRepository.save(editedComment);
    }

    public void delete(Long userId, Long commentId) throws InvalidAccessException {
        User user = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);
        Comment deletedComment = commentRepository.findById(commentId)
                .orElseThrow(CommentNotFoundException::new);
        // 댓글을 쓴 유저와 삭제하는 유저가 다른 경우
        if(user.getId()!=deletedComment.getUser().getId()){
            throw new InvalidAccessException();
        }
        // child가 있는 부모인 경우
        if(!deletedComment.getChildren().isEmpty()){
            // 댓글을 삭제하는 것이 아닌 삭제된 댓글이라고 표시
            deletedComment.markAsDeleted();
            commentRepository.save(deletedComment);
        }
        else {
            // child가 없는 부모 또는 child인 경우
            commentRepository.delete(deletedComment);
        }
    }

}
