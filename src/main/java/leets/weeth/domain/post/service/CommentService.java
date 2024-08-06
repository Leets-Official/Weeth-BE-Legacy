package leets.weeth.domain.post.service;

import org.springframework.transaction.annotation.Transactional;
import leets.weeth.domain.post.dto.RequestCommentDTO;
import leets.weeth.domain.post.entity.Comment;
import leets.weeth.domain.post.entity.Post;
import leets.weeth.domain.post.repository.CommentRepository;
import leets.weeth.domain.post.repository.PostRepository;
import leets.weeth.domain.user.entity.User;
import leets.weeth.domain.user.repository.UserRepository;
import leets.weeth.global.common.error.exception.custom.CommentNotFoundException;
import leets.weeth.global.common.error.exception.custom.PostNotFoundException;
import leets.weeth.global.common.error.exception.custom.UserMismatchException;
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

    @Transactional(rollbackFor = Exception.class)
    public void createComment(Long userId, Long postId, RequestCommentDTO requestCommentDTO) {
        User user = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);

        Post post = postRepository.findById(postId).orElseThrow(PostNotFoundException::new);

        Comment parent = null;
        if(requestCommentDTO.getParentCommentId() != null)
            parent = commentRepository.findById(requestCommentDTO.getParentCommentId())
                .orElseThrow(CommentNotFoundException::new);

        Comment comment = Comment.createComment(requestCommentDTO, post, user, parent);
        commentRepository.save(comment);

        if(parent == null)
            post.addComment(comment);
        else
            parent.addChild(comment);
    }

    @Transactional(rollbackFor = Exception.class)
    public void updateComment(Long userId, Long postId, Long commentId, RequestCommentDTO requestCommentDTO) throws UserMismatchException {
        User currentUser = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);
        Post post = postRepository.findById(postId)
                .orElseThrow(PostNotFoundException::new);
        Comment commentToEdit = commentRepository.findById(commentId)
                .orElseThrow(CommentNotFoundException::new);

        // 댓글을 쓴 유저와 수정하는 유저가 같은지 확인
        checkCommentAuthor(currentUser, userId);

        commentToEdit.updateComment(requestCommentDTO);
        commentRepository.save(commentToEdit);
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteComment(Long userId, Long commentId) throws UserMismatchException {
        User currentUser = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);
        Comment commentToDelete = commentRepository.findById(commentId)
                .orElseThrow(CommentNotFoundException::new);

        // 댓글을 쓴 유저와 삭제하는 유저가 같은지 확인
        checkCommentAuthor(currentUser, userId);

        Post currentPost = commentToDelete.getPost();
        // 댓글이 child인 경우
        // 댓글이 child이고 parent가 없는 경우
        // 댓글이 parent이고 child가 없는 경우
        // 댓글이 parent이고 child가 있는 경우

        // 지우고자 하는 댓글이 child인 경우 및 child가 없는 부모인 경우
        if(commentToDelete.getChildren().isEmpty()){
            Comment parentComment = findParentComment(commentToDelete .getId());
            commentRepository.delete(commentToDelete);
            // child인 경우
            if (parentComment != null) {
                parentComment.getChildren().remove(commentToDelete);
                // 부모 댓글이 삭제되었고 부모 댓글이 자식 댓글이 없는 경우
                if(parentComment.getIsDeleted()&&parentComment.getChildren().isEmpty()){
                    // parentComment의 자식도 없고 본인도 삭제된 상태이므로
                    currentPost.getParentComments().remove(parentComment);
                    commentRepository.delete(parentComment);
                }
            }
        }
        else{
            // child가 있는 부모인 경우
            // 댓글을 삭제하는 것이 아닌 삭제된 댓글이라고 표시
            commentToDelete.markAsDeleted();
            commentRepository.save(commentToDelete);
        }
        postRepository.save(currentPost);
    }

    // 댓글의 부모 찾기
    private Comment findParentComment(Long commentId) {
        List<Comment> allComments = commentRepository.findAll();
        for (Comment comment : allComments) {
            if (comment.getChildren().stream().anyMatch(child -> child.getId().equals(commentId))) {
                return comment;
            }
        }
        return null; // 부모 댓글을 찾지 못한 경우
    }

    private void checkCommentAuthor(User user, Long userId) throws UserMismatchException {
        User currentUser = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        if(!user.equals(currentUser)){
            throw new UserMismatchException();
        }
    }

}
