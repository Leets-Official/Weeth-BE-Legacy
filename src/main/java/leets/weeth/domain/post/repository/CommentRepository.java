package leets.weeth.domain.post.repository;

import leets.weeth.domain.post.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {

}
