package leets.weeth.domain.post.service;

import leets.weeth.domain.post.dto.RequestCommentDTO;
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

@RequiredArgsConstructor
@Service
public class CommentService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;

    public void create(Long userId, Long postId, RequestCommentDTO requestCommentDTO) throws InvalidAccessException {
        User user = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);
        Post currentPost = postRepository.findById(postId).orElseThrow(PostNotFoundException::new);
        
        Comment newComment = Comment.createComment(requestCommentDTO, currentPost, user);
        Comment parentComment;
        commentRepository.save(newComment);
        // child인 경우(부모가 있는 경우)
        if(requestCommentDTO.getParentId()!=null){
            parentComment = commentRepository.findById(requestCommentDTO.getParentId())
                    .orElseThrow(CommentNotFoundException::new);
            parentComment.addChild(newComment);
            commentRepository.save(parentComment);
        }
        else{
            currentPost.addComment(newComment);
            postRepository.save(currentPost);
        }
        currentPost.calculateTotalComments();
        postRepository.save(currentPost);
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
        Post currentPost = deletedComment.getPost();
        // child가 있는 부모인 경우
        if(!deletedComment.getChildren().isEmpty()){
            // 댓글을 삭제하는 것이 아닌 삭제된 댓글이라고 표시
            deletedComment.markAsDeleted();
            commentRepository.save(deletedComment);
        }
        else {
            Comment parentComment = findParentComment(deletedComment.getId());
            // child인 경우
            if (parentComment != null) {
                parentComment.getChildren().remove(deletedComment);
                commentRepository.save(parentComment);
            }
            // child인 경우 및 자식이 없는 부모인 경우
            commentRepository.delete(deletedComment);
        }
        currentPost.calculateTotalComments();
        postRepository.save(currentPost);
    }

    // 댓글의 부모를 찾는 메서드
    private Comment findParentComment(Long commentId) {
        // 부모 댓글을 찾는 로직을 여기에 추가합니다.
        // 예를 들어, 댓글의 자식 댓글을 모두 로드하여 부모를 찾을 수 있습니다.
        List<Comment> allComments = commentRepository.findAll();
        for (Comment comment : allComments) {
            if (comment.getChildren().stream().anyMatch(child -> child.getId().equals(commentId))) {
                return comment;
            }
        }
        return null; // 부모 댓글을 찾지 못한 경우
    }

}
