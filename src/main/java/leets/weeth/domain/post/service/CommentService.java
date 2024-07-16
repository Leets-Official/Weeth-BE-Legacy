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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentService {
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CommentRepository commentRepository;

    public List<ResponseCommentDTO> comments(Long articleId) {
        return commentRepository.findByPostId(articleId)
                .stream()
                .map(ResponseCommentDTO::createResponseCommentDto)
                .collect(Collectors.toList());
    }

    public void create(String email, Long articleId, RequestCommentDTO requestCommentDTO) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("failed to add post! no such user"));
        Post targetPost = postRepository.findById(articleId).orElseThrow(()->new EntityNotFoundException("failed to add post! no such post"));

        Comment newComment = Comment.createComment(requestCommentDTO, targetPost, user);
        commentRepository.save(newComment);
    }
}
