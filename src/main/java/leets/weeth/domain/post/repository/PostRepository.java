package leets.weeth.domain.post.repository;

import leets.weeth.domain.post.entity.Post;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.ArrayList;
import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    ArrayList<Post> findAll();
    List<Post> findByUserId(Long userId, Sort sort);

}
