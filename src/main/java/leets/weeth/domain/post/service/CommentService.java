package leets.weeth.domain.post.service;

import jakarta.persistence.EntityNotFoundException;
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
        return commentRepository.findByPostId(postId)
                .stream()
                .map(ResponseCommentDTO::createResponseCommentDto)
                .collect(Collectors.toList());
    }
    //myComments

    public void create(Long userId, Long postId, RequestCommentDTO requestCommentDTO) {
        User user = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);
        Post createdPost = postRepository.findById(postId).orElseThrow(()->new EntityNotFoundException("failed to add post! no such post"));

        Comment newComment = Comment.createComment(requestCommentDTO, createdPost, user);
        commentRepository.save(newComment);
    }

    public void updateComment(Long userId, Long postId, Long commentId, RequestCommentDTO requestCommentDTO) throws InvalidAccessException {
        User user = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);
        Post post = postRepository.findById(postId)
                .orElseThrow(PostNotFoundException::new);
        Comment editedComment = commentRepository.findById(commentId)
                .orElseThrow(CommentNotFoundException::new);
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
        if(user.getId()!=deletedComment.getUser().getId()){
            throw new InvalidAccessException();
        }
        commentRepository.delete(deletedComment);
    }
}
